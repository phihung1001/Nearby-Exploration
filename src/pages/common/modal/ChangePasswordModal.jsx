import React, { useState, useEffect } from "react";
import { Modal, Form, Input, notification } from "antd";
import { changePassword } from "../../../services/authService";

export default function ChangePasswordModal({ visible, onClose }) {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleOk = async () => {
    try {
      const { oldPassword, newPassword } = await form.validateFields();
      setLoading(true);
      await changePassword(oldPassword, newPassword);
      notification.success({ message: "Đổi mật khẩu thành công!" });
      form.resetFields(); // reset sau khi thành công
      onClose();
    } catch (error) {
      notification.error({
        message: "Đổi mật khẩu thất bại!",
        description: error.message
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!visible) {
      form.resetFields(); // reset mỗi khi modal đóng
    }
  }, [visible, form]);

  return (
    <Modal
      title="Đổi mật khẩu"
      open={visible}
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
