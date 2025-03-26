import React, { useState } from "react";
import { Card, Descriptions, Form, Input, Button, notification } from "antd";
import { MailOutlined, PhoneOutlined } from "@ant-design/icons";
import styles from "./Profile.module.css";

export default function InfoPanel({ userData, onUpdateSuccess }) {
  const [editMode, setEditMode] = useState(false);
  const [form] = Form.useForm();

  // Khi bấm "Chỉnh sửa", đổ dữ liệu vào form và bật edit mode
  const handleEditClick = () => {
    form.setFieldsValue({
      fullName: userData.fullName,
      email: userData.email,
      age: userData.age,
      phoneNumber: userData.phoneNumber,
      address: userData.address,
    });
    setEditMode(true);
  };

  const handleCancel = () => {
    setEditMode(false);
  };

  // Hàm gọi API update
  const handleFinish = async (values) => {

      const token = localStorage.getItem("token");
      try{
      const response = await fetch(`http://localhost:8080/customer/update/${userData.id}`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      });

      const updatedData = await response.json();
      if (!response.ok) {
        throw new Error(updatedData.message);
      }
      console.log("response",response);

      notification.success({ message: "Cập nhật thông tin thành công!" });
      setEditMode(false);
      if (onUpdateSuccess) {
        onUpdateSuccess(updatedData);
      }
    }catch (error) {
        notification.error({ message: "Cập nhật thất bại!", description: error.message });
      }

  };

  if (editMode) {
    return (
      <Card className={styles.profileCard}>
        <Form form={form} layout="vertical" onFinish={handleFinish}>
          <Form.Item
            label="Họ và tên"
            name="fullName"
            rules={[{ required: true, message: "Vui lòng nhập họ và tên" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true, message: "Vui lòng nhập email" }]}
          >
            <Input prefix={<MailOutlined />} />
          </Form.Item>
          <Form.Item
            label="Độ tuổi"
            name="age"
            rules={[{ required: true, message: "Vui lòng nhập độ tuổi" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Số điện thoại"
            name="phoneNumber"
            rules={[{ required: true, message: "Vui lòng nhập số điện thoại" }]}
          >
            <Input prefix={<PhoneOutlined />} />
          </Form.Item>
          <Form.Item label="Địa chỉ" name="address">
            <Input />
          </Form.Item>
          <Form.Item>
            <Button onClick={handleCancel} style={{ marginRight: 8 }}>
              Hủy
            </Button>
            <Button type="primary" htmlType="submit">
              Lưu thay đổi
            </Button>
          </Form.Item>
        </Form>
      </Card>
    );
  }

  return (
    <Card className={styles.profileCard}>
      <Descriptions title="Thông tin cá nhân" bordered column={1}>
        <Descriptions.Item label="Họ và tên">{userData.fullName}</Descriptions.Item>
        <Descriptions.Item label="Email">
          <MailOutlined /> {userData.email}
        </Descriptions.Item>
        <Descriptions.Item label="age">
          {userData.age}
        </Descriptions.Item>
        <Descriptions.Item label="Số điện thoại">
          <PhoneOutlined /> {userData.phoneNumber}
        </Descriptions.Item>
        <Descriptions.Item label="Địa chỉ">
          {userData.address || "Chưa cập nhật"}
        </Descriptions.Item>
      </Descriptions>
      <Button type="primary" style={{ marginTop: 16 }} onClick={handleEditClick}>
        Chỉnh sửa thông tin
      </Button>
    </Card>
  );
}
