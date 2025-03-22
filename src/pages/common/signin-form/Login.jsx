import React from 'react';
import { useNavigate } from 'react-router-dom'; 
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input, Flex, Modal } from 'antd';
import styles from './Login.module.css'; // Import CSS Module


export default function Login() {
  const navigate = useNavigate(); 
  const onFinish = async (values) => {
    try {
      const response = await fetch('http://localhost:8080/auth/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(values)
      });
      const result = await response.json();
      if (!response.ok) {
          const modal = Modal.error({
            centered: true,
            title: 'Đăng nhập thất bại',
            content: result.message,
          });
          setTimeout(() => {
            modal.destroy();
        }, 2000);
      } else {
        const modal = Modal.success({
          centered: true,
          title: 'Đăng nhập thành công',
          content: 'Chào mừng bạn đến với hệ thống của chúng tôi',
        });
        navigate('/');
        localStorage.setItem('token', result.accessToken);
        setTimeout(() => {
          modal.destroy();
        }, 1000);
    }
    } catch (error) {
      console.error('Lỗi trong quá trình đăng nhập:', error);
    }
    
    
  };
  return (
    <div className={styles.loginContainer}>
      <div className={styles.pageLeft}>
      </div>
      <div className={styles.pageRight}>
        <div className={styles.formLoginContainer}>
          <div className={styles.formTitle}>
            <h2> Đăng nhập </h2>
          </div>
            <Form
                   className={styles.antForm} 
                   name="login"
                   initialValues={{ remember: true, }}
                   style={{ maxWidth: 360,}}
                   onFinish={onFinish}
            >
                <Form.Item
                  className={styles.antFormItem} 
                  name="email"
                  rules={[{ required: true,message: 'Please input your email!',},]}
                >
                  <Input prefix={<UserOutlined />} placeholder="Email" />
                </Form.Item>
                <Form.Item
                  className={styles.antFormItem} 
                  name="password"
                  rules={[
                    {
                      required: true,
                      message: 'Please input your Password!',
                    },
                  ]}
                >
                  <Input prefix={<LockOutlined />} type="password" placeholder="Password" />
                </Form.Item>
                <Form.Item>
                  <Flex justify="space-between" align="center">
                    <Form.Item name="remember" valuePropName="checked" noStyle>
                      <Checkbox>Lưu mật khẩu</Checkbox>
                    </Form.Item>
                    <a onClick={() => navigate('/auth/reset-password') }>
                        Quên mật khẩu
                     </a>
                  </Flex>
                </Form.Item>
                <Form.Item>
                  <Button block type="primary" htmlType="submit">
                    Đăng nhập ngay
                  </Button>
                  Chưa có tài khoản,{' '}
                  <a onClick={() => navigate('/auth/signup')}>
                    Đăng ký ngay !
                  </a>
                </Form.Item>
            </Form>
        </div>
      </div>
    </div>
  );
}
