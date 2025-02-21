import React from "react";
import { Button } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import styles from "./AuthButton.module.css"; // Import CSS

export default function AuthButton() {
  const navigate = useNavigate();
  
  return (
    <Button 
      className={styles.authButton} 
      onClick={() => navigate('/signin')} 
      icon={<UserOutlined />} 
      type="primary"
    >
      Đăng nhập
    </Button>
  );
}
