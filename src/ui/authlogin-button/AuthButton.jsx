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
  
    async function refreshAccessToken() {
      try {
        const res = await fetch("http://localhost:8080/auth/refresh", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include"  // nếu backend set HttpOnly cookie cho refresh token
        });
  
        if (res.ok) {
          const data = await res.json();
          localStorage.setItem("token", data.accessToken);
          const decoded = jwtDecode(data.accessToken);
          setUser(decoded);
          setUsername(decoded.fullName);
        } else {
          console.log("Refresh token không hợp lệ, cần đăng nhập lại");
          localStorage.removeItem("token");
          setUser(null);
          setUsername(null);
        }
      } catch (err) {
        console.error("Lỗi khi gọi /auth/refresh:", err);
      }
    }
  
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const currentTime = Date.now() / 1000;
        if (decoded.exp && decoded.exp < currentTime) {
          console.log("Token đã hết hạn, đang refresh...");
          refreshAccessToken();  // gọi refresh nếu hết hạn
        } else {
          setUser(decoded);
          setUsername(decoded.fullName);
        }
      } catch (error) {
        console.error("Lỗi giải mã token:", error);
        localStorage.removeItem("token");
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
