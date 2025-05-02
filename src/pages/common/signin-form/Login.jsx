import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input, Flex, Modal } from 'antd';
import styles from './Login.module.css';
import { loginUser } from '../../../services/authService'; // import hàm login

export default function Login() {
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const result = await loginUser(values);

      const modal = Modal.success({
        centered: true,
        title: 'Đăng nhập thành công',
        content: 'Chào mừng bạn đến với hệ thống của chúng tôi',
      });

      localStorage.setItem('token', result.accessToken);
      setTimeout(() => {
        modal.destroy();
        navigate('/');
      }, 1000);
    } catch (error) {
      const modal = Modal.error({
        centered: true,
        title: 'Đăng nhập thất bại',
        content: error.message,
      });

      setTimeout(() => {
        modal.destroy();
      }, 2000);
    }
  };

  return (
    <div className={styles.loginContainer}>
      <div className={styles.pageLeft}></div>
      <div className={styles.pageRight}>
        <div className={styles.formLoginContainer}>
          <div className={styles.formTitle}>
            <h2>Đăng nhập</h2>
          </div>
          <Form
            className={styles.antForm}
            name="login"
            initialValues={{ remember: true }}
            style={{ maxWidth: 360 }}
            onFinish={onFinish}
          >
            <Form.Item
              className={styles.antFormItem}
              name="email"
              rules={[{ required: true, message: 'Vui lòng nhập email!' }]}
            >
              <Input prefix={<UserOutlined />} placeholder="Email" />
            </Form.Item>
            <Form.Item
              className={styles.antFormItem}
              name="password"
              rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
            >
              <Input prefix={<LockOutlined />} type="password" placeholder="Mật khẩu" />
            </Form.Item>
            <Form.Item>
              <Flex justify="space-between" align="center">
                <Form.Item name="remember" valuePropName="checked" noStyle>
                  <Checkbox>Lưu mật khẩu</Checkbox>
                </Form.Item>
                <Link to="/auth/reset-password">Quên mật khẩu</Link>
              </Flex>
            </Form.Item>
            <Form.Item>
              <Button block type="primary" htmlType="submit">
                Đăng nhập ngay
              </Button>
              Chưa có tài khoản, <Link to="/auth/signup">Đăng ký ngay</Link>
            </Form.Item>
          </Form>
        </div>
      </div>
    </div>
  );
}
