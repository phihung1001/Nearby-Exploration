import React, { useState } from 'react';
import { Form, Input, Button, Card ,notification} from 'antd';

const AddRestaurantPanel = ({onCancel}) => {
    const [loading, setLoading] = useState(true);
  
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    try {
         const token = localStorage.getItem("token");
         const response = await fetch(`http://localhost:8080/public/restaurant/register`, {
           method: "POST",
           headers: {
             "Content-Type": "application/json",
             Authorization: `Bearer ${token}`
           },
           body: JSON.stringify(values)
         });
         const res = await response.json();
         console.log("response",res);
         if (!response.ok) throw new Error(`${res.message}`);
         notification.success({
           message: "Thêm mới nhà hàng thành công!"
         });
         onCancel(); // quay về danh sách
       } catch (error) {
           notification.error({
               message:"Thêm mới thất bại ",
               description: error.message
            });
       }

  };

  return (
    <Card title="Thêm Nhà Hàng Mới" style={{ maxWidth: 600, margin: '0 auto' }}>
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
      >
        <Form.Item
          label="Tên nhà hàng"
          name="name"
          rules={[{ required: true, message: 'Tên nhà hàng không được để trống' }]}
        >
          <Input placeholder="Nhập tên nhà hàng" />
        </Form.Item>

        <Form.Item
          label="Địa chỉ"
          name="address"
          rules={[{ required: true, message: 'Địa chỉ không được để trống' }]}
        >
          <Input placeholder="Nhập địa chỉ" />
        </Form.Item>

        <Form.Item
          label="Số điện thoại"
          name="phone"
          rules={[{ required: true, message: 'Số điện thoại không được để trống' }]}
        >
          <Input placeholder="Nhập số điện thoại" />
        </Form.Item>

        <Form.Item
          label="Email"
          name="email"
          rules={[{ type: 'email', message: 'Email không hợp lệ!' }]}
        >
          <Input placeholder="Nhập email (nếu có)" />
        </Form.Item>

        <Form.Item
          label="Thành phố"
          name="city"
        >
          <Input placeholder="Nhập thành phố" />
        </Form.Item>

        <Form.Item
          label="Quận/Huyện"
          name="district"
        >
          <Input placeholder="Nhập quận/huyện" />
        </Form.Item>

        <Form.Item
          label="Số nhà"
          name="houseNumber"
        >
          <Input placeholder="Nhập số nhà" />
        </Form.Item>

        <Form.Item
          label="Link ảnh"
          name="photoUrl"
        >
          <Input placeholder="Nhập link ảnh" />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit">
            Thêm Nhà Hàng
          </Button>
          <Button style={{ marginLeft: 8 }} onClick={onCancel}>Huỷ</Button>
          
        </Form.Item>
      </Form>
    </Card>
  );
};

export default AddRestaurantPanel;
