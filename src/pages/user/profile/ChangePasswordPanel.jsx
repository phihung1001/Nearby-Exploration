import React from "react";
import { Card, Form, Input, Button, notification } from "antd";

export default function ChangePasswordPanel({id}) {
  const [form] = Form.useForm();

  const handleChangePassword = async (values) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/customer/update/password/${id}`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(values)
      });
      const updatedData = await response.json();
      if (!response.ok) {
        throw new Error(`${updatedData.message}`);
      }
      notification.success({ message: "Đổi mật khẩu thành công!" });
      form.resetFields();
    } catch (error) {
      notification.error({ message: "Đổi mật khẩu thất bại!", description: error.message });
    }
  };

  return (
    <Card>
      <Form form={form} layout="vertical" onFinish={handleChangePassword}>
        <Form.Item
          label="Mật khẩu cũ"
          name="oldPassword"
          rules={[{ required: true, message: "Vui lòng nhập mật khẩu cũ" }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="Mật khẩu mới"
          name="newPassword"
          rules={[{ required: true, message: "Vui lòng nhập mật khẩu mới" }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="Xác nhận mật khẩu mới"
          name="confirmNewPassword"
          dependencies={["newPassword"]}
          rules={[
            { required: true, message: "Vui lòng xác nhận mật khẩu mới" },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newPassword") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error("Mật khẩu xác nhận không khớp"));
              }
            })
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Button type="primary" htmlType="submit">
          Xác nhận
        </Button>
      </Form>
    </Card>
  );
}
