import React from 'react';
import { Form, Input, Button, message } from 'antd';
import styles from './ResetPassword.module.css';

export default function ResetPassword() {
  const onFinish = (values) => {
    console.log('Email:', values.email);
    message.success('Vui lòng kiểm tra email để đặt lại mật khẩu!');
  };

  return (

    <div className={styles.resetContainer}>
      <div className={styles.pageLeft}>
      </div>
      <div className={styles.pageRight}>
        <div className={styles.resetContainer}>
          <div className={styles.formReset}>
          <div className={styles.formResetTitle}> <h2>Đặt lại mật khẩu</h2>
          </div>
        
          <Form
              name="resetPassword"
              onFinish={onFinish}
              layout="vertical"
              requiredMark={false}
          >
              <Form.Item
              label="Email"
              name="email"
              rules={[
                  { required: true, message: 'Vui lòng nhập email!' },
                  { type: 'email', message: 'Email không hợp lệ!' },
              ]}
              >
              <Input placeholder="Nhập email của bạn" />
              </Form.Item>

              <Form.Item>
              <Button type="primary" htmlType="submit" block>
                  Gửi yêu cầu
              </Button>
              </Form.Item>
          </Form>
          </div>
        </div>
      </div>
    </div>

   
  );
}
