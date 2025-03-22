import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Card, Avatar, Descriptions, Spin } from "antd";
import { UserOutlined, MailOutlined, PhoneOutlined } from "@ant-design/icons";
import axios from "axios";
import styles from "./Profile.module.css";

export default function Profile() {
  const { id } = useParams();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`/http://localhost:8080/public/customer/${id}`);
        setUserData(response.data);
      } catch (error) {
        console.error("Lỗi khi tải thông tin người dùng:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, [id]);

  if (loading) {
    return <Spin size="large" className={styles.spinner} />;
  }

  if (!userData) {
    return <p className={styles.errorText}>Không tìm thấy người dùng.</p>;
  }

  return (
    <Card className={styles.profileCard}>
      <Avatar size={100} src={userData.avatar || <UserOutlined />} />
      <Descriptions title="Thông tin cá nhân" bordered column={1}>
        <Descriptions.Item label="Họ và tên">{userData.fullName}</Descriptions.Item>
        <Descriptions.Item label="Email">
          <MailOutlined /> {userData.email}
        </Descriptions.Item>
        <Descriptions.Item label="Số điện thoại">
          <PhoneOutlined /> {userData.phone}
        </Descriptions.Item>
        <Descriptions.Item label="Địa chỉ">{userData.address || "Chưa cập nhật"}</Descriptions.Item>
      </Descriptions>
    </Card>
  );
}
