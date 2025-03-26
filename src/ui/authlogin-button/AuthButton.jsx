import React, { useState, useEffect } from "react";
import { Button, Popover, List, Avatar } from "antd";
import {
  UserOutlined,
  HeartOutlined,
  BellOutlined,
  MessageOutlined,
  LogoutOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import styles from "./AuthButton.module.css";
import { jwtDecode } from "jwt-decode";

export default function AuthButton() {
  const navigate = useNavigate();
  const [username, setUsername] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        if (decoded && typeof decoded === "object") {
          setUser(decoded);
          console.log("decode",decoded);
        }
        
        setUsername(decoded.fullName);
      } catch (error) {
        console.error("Lỗi giải mã token:", error);
      }
    }
  }, []);

  const handleClick = () => {
    if (!username && !user?.sub) {
      navigate("/auth/signin");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setUsername(null);
    setUser(null);
    navigate("/auth/signin");
  };

  const modalItems = [
    { title: "Thông tin cá nhân", icon: <UserOutlined />, onClick: () => navigate(`/profile/${user.id}`)},
    { title: "Đã lưu", icon: <HeartOutlined />, onClick: () => navigate("/saved-stores") },
    { title: "Thông báo", icon: <BellOutlined />, onClick: () => navigate("/notifications") },
    { title: "Tin nhắn", icon: <MessageOutlined />, onClick: () => navigate("/messages") },
    { title: "Đăng xuất", icon: <LogoutOutlined />, onClick: handleLogout },
  ];

  const content = (
    <List
      itemLayout="horizontal"
      dataSource={modalItems}
      renderItem={(item) => (
      <List.Item
        className={styles.authModalItem}
        onClick={item.onClick}
      >
      <List.Item.Meta
          avatar={<Avatar icon={item.icon} className={styles.authModalItemIcon} />}
          title={<span className={styles.authModalItemText}>{item.title}</span>}
      />
      </List.Item>

      )}
    />
  );

  return (
    <Popover
      content={<div className={styles.authModal}>{content}</div>}
      title={<div className={styles.authModalTitle}>Xin chào {user?.fullName || "bạn"}!</div>}
      trigger="click"
      onClick={handleClick}
    >

      <Button
        className={styles.authButton}
        icon={<UserOutlined />}
        type="primary"
      >
        {user?.fullName || user?.sub || "Đăng nhập"}
      </Button>
    </Popover>
  );
}
