import React, { useEffect, useState } from "react";
import { Input, Button, Form, notification } from "antd";

export default function EditRestaurantPanel({ restaurantData, onCancel }) {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (restaurantData) {
      setLoading(false);
    }
  }, [restaurantData]);

  const handleFinish = async (values) => {
    try {
      const token = localStorage.getItem("token");
      const res = await fetch(`http://localhost:8080/public/restaurant/update/${restaurantData.id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(values)
      });
      const resJson = await res.json();
      if (!res.ok) throw new Error(resJson.message);
      notification.success({
        message: "Cập nhật nhà hàng thành công!"
    });
      onCancel(); // quay về danh sách
    } catch (error) {
        notification.error({
            message:"Cập nhật thất bại ",
            description: error.message}
        );
    }
  };

  if (loading) return <p>Đang tải dữ liệu...</p>;
  if (!restaurantData) return <p>Không tìm thấy nhà hàng.</p>;

  return (
    <div style={{ padding: 20, background: "#fff", borderRadius: 8 }}>
      <h2>Chỉnh sửa nhà hàng: {restaurantData.name}</h2>

      <Form
        layout="vertical"
        initialValues={restaurantData}
        onFinish={handleFinish}
      >
        <Form.Item
          label="Tên nhà hàng"
          name="name"
          rules={[{ required: true, message: "Vui lòng nhập tên nhà hàng" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Địa chỉ"
          name="address"
          rules={[{ required: true, message: "Vui lòng nhập địa chỉ" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Số điện thoại"
          name="phone"
          rules={[{ required: true, message: "Vui lòng nhập số điện thoại" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item label="Email" name="email">
          <Input />
        </Form.Item>

        <Form.Item label="Tỉnh / Thành phố" name="city">
          <Input />
        </Form.Item>

        <Form.Item label="Quận / Huyện" name="district">
          <Input />
        </Form.Item>

        <Form.Item label="Ảnh đại diện" name="photoUrl">
          <Input />
        </Form.Item>

        <Form.Item label="Số nhà" name="houseNumber">
          <Input />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">Lưu thay đổi</Button>
          <Button style={{ marginLeft: 8 }} onClick={onCancel}>Huỷ</Button>
        </Form.Item>
      </Form>
    </div>
  );
}
