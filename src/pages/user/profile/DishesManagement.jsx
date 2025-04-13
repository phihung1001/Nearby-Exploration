import React, { useEffect, useState } from "react";
import { Select, Table, Button, Modal, Form, Input, notification } from "antd";

const { Option } = Select;

export default function DishManager() {
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [dishes, setDishes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [editingId, setEditingId] = useState(null);

  // Fetch danh sách nhà hàng khi load trang
  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await fetch("http://localhost:8080/public/restaurant/list-restaurant/user", {
          headers: { Authorization: `Bearer ${token}` }
        });
        const data = await res.json();
        setRestaurants(data.content || []);
      } catch (error) {
        notification.error({ message: "Lỗi", description: "Không thể tải danh sách nhà hàng." });
      }
    };
    fetchRestaurants();
  }, []);

  // Fetch danh sách món ăn theo nhà hàng
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
    form.resetFields(); // clear form khi đổi nhà hàng
  };

  // Xoá món ăn
  const handleDelete = async (dishId) => {
    Modal.confirm({
      title: "Xác nhận xoá",
      content: "Bạn có chắc chắn muốn xoá món ăn này không?",
      okText: "Xoá",
      cancelText: "Huỷ",
      onOk: async () => {
        try {
          const token = localStorage.getItem("token");
          await fetch(`http://localhost:8080/public/dishes/delete/${dishId}`, {
            method: "DELETE",
            headers: { Authorization: `Bearer ${token}` }
          });
          notification.success({ message: "Xoá thành công!" });
          fetchDishes(selectedRestaurant);
        } catch (error) {
          notification.error({ message: "Xoá thất bại!", description: error.message });
        }
      }
    });
  };

  // Lưu món ăn (thêm mới hoặc cập nhật)  
  const handleSaveDish = async (values) => {
    try {
      const token = localStorage.getItem("token");
      values.restaurantId = selectedRestaurant;
      console.log("values", values);
      if (values.id) {
        try {
          // Update món ăn
          const res = await fetch(`http://localhost:8080/public/dishes/update/${values.id}`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`
            },
            body: JSON.stringify(values)
          });
          console.log("res", res);
          const response = await res.json();
          if (!res.ok) throw new Error(response.message || "Cập nhật món ăn thất bại!");
          notification.success({ message: "Cập nhật món ăn thành công!" });
          setEditingId(null);
        }catch (error) {
          notification.error({ message: "Lỗi", description: error.message });
        }
    
      } else {
        // Thêm mới món ăn
        const res = await fetch(`http://localhost:8080/public/dishes/add`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
          },
          body: JSON.stringify(values)
        });
        const response = await res.json();
        console.log("responseAdd", response);
        if (!res.ok) throw new Error(response.message || "Thêm món ăn thất bại!");
        notification.success({ message: "Thêm món ăn thành công!" });
      }

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
                <Button 
                  onClick={() => {
                    if (!selectedRestaurant) {
                      notification.warning({ message: "Vui lòng chọn nhà hàng trước!" });
                      return;
                    }
                    form.setFieldsValue(record);
                    setEditingId(record.id);  // đánh dấu đang sửa
                  }}
                >
                  Sửa
                </Button>
                <Button danger onClick={() => handleDelete(record.id)} style={{ marginLeft: 8 }}>
                  Xoá
                </Button>
              </>
            )
          }
        ]}
        pagination={{
          pageSize: 5,
          total: dishes.length
        }}
      />

      <h3 style={{ marginTop: 10 }}>Thêm / Sửa món ăn</h3>
      <Form layout="vertical" form={form} onFinish={handleSaveDish}>
        <Form.Item name="id" hidden>
          <Input />
        </Form.Item>
        <Form.Item label="Tên món ăn" name="name" rules={[{ required: true, message: "Vui lòng nhập tên món ăn!" }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Mô tả" name="description">
          <Input />
        </Form.Item>
        <Form.Item label="Link ảnh" name="image">
          <Input />
        </Form.Item>
        <Form.Item label="Giá" name="price" rules={[{ required: true, message: "Vui lòng nhập giá!" }]}>
          <Input type="number" />
        </Form.Item>
        <Button type="primary" htmlType="submit" disabled={!selectedRestaurant}>
            {editingId ? "Cập nhật món ăn" : "Thêm món ăn"}
        </Button>
        {form.getFieldValue("id") && (
          <Button style={{ marginLeft: 8 }} onClick={() => {
            form.resetFields();
            setEditingId(null); 
          }}>
            Huỷ chỉnh sửa
          </Button>
        )}
      </Form>
    </div>
  );
}
