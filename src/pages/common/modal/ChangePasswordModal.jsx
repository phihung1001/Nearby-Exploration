import React, { useState } from "react";
import { Modal, Form, Input, Button, notification } from "antd";

export default function ChangePasswordModal({ visible, onClose }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleOk = async () => {
    try {
      setLoading(true);
      const values = await form.validateFields();
      // Gọi API đổi mật khẩu
      const token = localStorage.getItem("token");
      const response = await fetch("http://localhost:8080/auth/change-password", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(values)
      });
      if (!response.ok) {
        throw new Error(`Lỗi ${response.status}`);
      }
      notification.success({ message: "Đổi mật khẩu thành công!" });
      onClose(); // Đóng modal sau khi đổi mật khẩu
    } catch (error) {
      console.error(error);
      notification.error({ message: "Đổi mật khẩu thất bại!", description: error.message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title="Đổi mật khẩu"
      visible={visible}
      onOk={handleOk}
      onCancel={onClose}
      confirmLoading={loading}
      okText="Xác nhận"
      cancelText="Hủy"
    >
      <Form form={form} layout="vertical">
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
      </Form>
    </Modal>
  );
}
