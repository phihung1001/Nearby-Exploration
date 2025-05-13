import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import backgroundLogin from '../../../assets/Img/loginpage1.jpg';
import styles from './Register.module.css';

import {
  Button,
  Checkbox,
  Form,
  Input,
  Select,
  Modal
} from 'antd';
import { registerUser } from '../../../services/authService'; 

const { Option } = Select;

const formItemLayout = {
  labelCol: { xs: { span: 24 }, sm: { span: 8 } },
  wrapperCol: { xs: { span: 24 }, sm: { span: 16 } },
};
const tailFormItemLayout = {
  wrapperCol: { xs: { span: 24, offset: 0 }, sm: { span: 16, offset: 8 } },
};

export default function Register() {
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const [autoCompleteResult, setAutoCompleteResult] = useState([]);

  const onFinish = async (values) => {
    try {
      const result = await registerUser(values);
      const modal = Modal.success({
        centered: true,
        title: 'Đăng kí thành công',
        content: 'Vui lòng đăng nhập để sử dụng hệ thống',
      });
      localStorage.setItem('token', result.token);
      setTimeout(() => {
        modal.destroy();
        navigate('/auth/signin');
      }, 1000);
    } catch (error) {
      const modal = Modal.error({
        centered: true,
        title: 'Đăng kí thất bại',
        content: error.message,
      });
      setTimeout(() => {
        modal.destroy();
      }, 2000);
    }
  };

  const onWebsiteChange = (value) => {
    setAutoCompleteResult(
      value ? ['.com', '.org', '.net'].map((domain) => `${value}${domain}`) : []
    );
  };

  const websiteOptions = autoCompleteResult.map((website) => ({
    label: website,
    value: website,
  }));

  return (
    <div className={styles.registerContainer}>
      <div className={styles.pageLeft}></div>
      <div className={styles.pageRight}>
        <div className={styles.formRegisterContainer}>
          <div className={styles.formTitle}>
            <h2> Đăng ký tài khoản </h2>
          </div>
          <Form
            className={styles.antForm}
            {...formItemLayout}
            form={form}
            name="register"
            onFinish={onFinish}
            style={{ maxWidth: 600 }}
            scrollToFirstError
          >
            <Form.Item
              className={styles.antFormItem}
              name="fullName"
              label="Name"
              tooltip="What do you want others to call you?"
              rules={[{ required: true, message: 'Yêu cầu nhập tên', whitespace: true }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="email"
              label="Email"
              rules={[
                { type: 'email', message: 'Định dạng mail không hợp lệ' },
                { required: true, message: 'Yêu cầu nhập email' },
              ]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="password"
              label="Mật khẩu"
              rules={[
                { required: true, message: 'Yêu cầu nhập mật khẩu' },
                { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' },
              ]}
              hasFeedback
            >
              <Input.Password />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="confirmPassword"
              label="Xác nhận mật khẩu "
              dependencies={['password']}
              hasFeedback
              rules={[
                { required: true, message: 'Yêu cầu nhập lại mật khẩu' },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    return !value || getFieldValue('password') === value
                      ? Promise.resolve()
                      : Promise.reject(new Error('Mật khẩu chưa khớp'));
                  },
                }),
              ]}
            >
              <Input.Password />
            </Form.Item>

             <Form.Item
              className={styles.antFormItem}
              name="age"
              label="Độ tuổi"
              rules={[{ required: true, message: 'Yêu cầu nhập tuổi của bạnbạn' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="address"
              label="Địa điểm"
              rules={[{ required: true, message: 'Yêu cầu nhập địa chỉ của bạn' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="phoneNumber"
              label="Số điện thoại"
              rules={[
                { required: true, message: 'Yêu cầu nhập số điện thoại!' },
                { pattern: /^[0-9]{10,11}$/, message: 'Số điện thoại không hợp lệ!' },
              ]}
            >
              <Input placeholder="Nhập số điện thoại" />
            </Form.Item>

            <Form.Item
              className={styles.antFormItem}
              name="gender"
              label="Giới tính"
              rules={[{ required: true, message: 'Yêu cầu chọn giới tính' }]}
            >
              <Select placeholder="Chọn giới tính">
                <Option value="male">Nam</Option>
                <Option value="female">Nữ</Option>
                <Option value="other">Khác</Option>
              </Select>
            </Form.Item>

            <Form.Item
              name="agreement"
              valuePropName="checked"
              rules={[
                {
                  validator: (_, value) =>
                    value ? Promise.resolve() : Promise.reject(new Error('Nên chấp nhận thỏa thuận')),
                },
              ]}
              {...tailFormItemLayout}
            >
              <Checkbox>
                Tôi đồng ý với <a href="#">điều khoản</a>
              </Checkbox>
            </Form.Item>

            <Form.Item {...tailFormItemLayout}>
              <Button type="primary" htmlType="submit">
                Đăng kí ngay
              </Button>
            </Form.Item>
          </Form>
        </div>
      </div>
    </div>
  );
}
