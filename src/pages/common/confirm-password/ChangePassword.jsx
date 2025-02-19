import React from 'react';
import { Form, Input, Button, message } from 'antd';
import styles from './ChangePassword.module.css';

export default function ChangePassword() {
  const onFinish = (values) => {
    if (values.password !== values.confirmPassword) {
      message.error('Mật khẩu xác nhận không khớp!');
      return;
    }
    console.log('Mật khẩu mới:', values.password);
    message.success('Mật khẩu đã được thay đổi thành công!');
  };

  return (
    <div className={styles.changeContainer}>
      <div className={styles.formChange}>
      <div className={styles.formResetTitle}>
        <h2>Thay đổi mật khẩu</h2>
      </div>
        <Form name="changePassword" onFinish={onFinish} layout="vertical" requiredMark={false}>
          {/* Mật khẩu mới */}
          <Form.Item
            label="Mật khẩu mới"
            name="password"
            rules={[
              { required: true, message: 'Vui lòng nhập mật khẩu mới!' },
              { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' },
            ]}
          >
            <Input.Password placeholder="Nhập mật khẩu mới" />
          </Form.Item>

          {/* Xác nhận mật khẩu */}
          <Form.Item
            label="Xác nhận mật khẩu"
            name="confirmPassword"
            dependencies={['password']}
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('Mật khẩu xác nhận không khớp!'));
                },
              }),
            ]}
          >
            <Input.Password placeholder="Xác nhận mật khẩu" />
          </Form.Item>

          {/* Nút Submit */}
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              Đổi mật khẩu
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
}
