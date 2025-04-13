import React, { useEffect, useState } from "react";
import { Select, Table, Button, Modal, Form, Input, notification } from "antd";

const { Option } = Select;

export default function DishManager() {
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [dishes, setDishes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchRestaurants = async () => {
      const token = localStorage.getItem("token");
      const res = await fetch("http://localhost:8080/public/restaurant/list-restaurant/user", {
        headers: { Authorization: `Bearer ${token}` }
      });
      const data = await res.json();
      console.log("data", data);
      setRestaurants(data.content || []);
    };
    fetchRestaurants();
  }, []);

  const fetchDishes = async (restaurantId) => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const res = await fetch(`http://localhost:8080/public/dishes/list/restaurant/${restaurantId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      const data = await res.json();
      setDishes(data);
    } catch (error) {
      notification.error({ message: "Lỗi", description: "Không thể tải danh sách món ăn." });
    } finally {
      setLoading(false);
    }
  };

  const handleSelectRestaurant = (id) => {
    setSelectedRestaurant(id);
    fetchDishes(id);
  };

  const handleDelete = async (dishId) => {
    try {
      const token = localStorage.getItem("token");
      await fetch(`http://localhost:8080/dish/delete/${dishId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` }
      });
      notification.success({ message: "Xoá thành công!" });
      fetchDishes(selectedRestaurant);
    } catch {
      notification.error({ message: "Xoá thất bại!" });
    }
  };

  const handleAddDish = async (values) => {
    try {
      const token = localStorage.getItem("token");
      values.restaurantId = selectedRestaurant;
      const res = await fetch(`http://localhost:8080/dish/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(values)
      });
      if (!res.ok) throw new Error("Thêm món ăn thất bại!");
      notification.success({ message: "Thêm món ăn thành công!" });
      fetchDishes(selectedRestaurant);
      form.resetFields();
    } catch (err) {
      notification.error({ message: "Lỗi", description: err.message });
    }
  };

  return (
    <div style={{ padding: 24 }}>
      <h2>Quản lý món ăn</h2>

      <Select 
        placeholder="Chọn nhà hàng" 
        style={{ width: 300, marginBottom: 20 }} 
        onChange={handleSelectRestaurant}
        value={selectedRestaurant}
      >
        {restaurants.map(r => (
          <Option key={r.id} value={r.id}>{r.name}</Option>
        ))}
      </Select>

      <Table
        dataSource={dishes}
        rowKey="id"
        loading={loading}
        columns={[
          { title: "Tên món", dataIndex: "name" },
          { title: "Giá", dataIndex: "price" },
          { title: "Mô tả", dataIndex: "description" },
          {
            title: "Thao tác",
            render: (_, record) => (
              <>
                <Button onClick={() => form.setFieldsValue(record)}>Sửa</Button>
                <Button danger onClick={() => handleDelete(record.id)} style={{ marginLeft: 8 }}>Xoá</Button>
              </>
            )
          }
        ]}
      />

      <h3 style={{ marginTop: 30 }}>Thêm món ăn</h3>
      <Form layout="vertical" form={form} onFinish={handleAddDish}>
        <Form.Item label="Tên món ăn" name="name" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Mô tả" name="description">
          <Input />
        </Form.Item>
        <Form.Item label="Link ảnh" name="image">
          <Input />
        </Form.Item>
        <Form.Item label="Giá" name="price" rules={[{ required: true }]}>
          <Input type="number" />
        </Form.Item>
        <Button type="primary" htmlType="submit" disabled={!selectedRestaurant}>
          Thêm món ăn
        </Button>
      </Form>
    </div>
  );
}
