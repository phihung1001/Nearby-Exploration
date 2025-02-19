import React from 'react';
import { useNavigate } from 'react-router-dom'; 
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input, Flex } from 'antd';
import styles from './Login.module.css'; // Import CSS Module


export default function Login() {
  const navigate = useNavigate(); 
  const onFinish = (values) => {
    console.log('Success:', values);
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
                  name="username"
                  rules={[{ required: true,message: 'Please input your Username!',},]}
                >
                  <Input prefix={<UserOutlined />} placeholder="Username" />
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
                    <a onClick={() => navigate('/reset-password') }>
                        Quên mật khẩu
                     </a>
                  </Flex>
                </Form.Item>
                <Form.Item>
                  <Button block type="primary" htmlType="submit">
                    Đăng nhập ngay
                  </Button>
                  Chưa có tài khoản,{' '}
                  <a onClick={() => navigate('/signup')}>
                    Đăng ký ngay !
                  </a>
                </Form.Item>
            </Form>
        </div>
      </div>
    </div>
  );
}
