import React from "react";
import { Avatar } from "antd";
import { UserOutlined } from "@ant-design/icons";
import styles from "./Profile.module.css";

export default function ProfileMenu({ userData, selectedMenu, onSelectMenu }) {
  return (
    <div className={styles.leftPanel}>
      <div className={styles.avatarWrapper}>
        <Avatar
          size={150}
          src={userData.avatar}
          icon={!userData.avatar && <UserOutlined />}
          className={styles.avatar}
        />
        <p>{userData.fullName}</p>
      </div>

      <ul className={styles.menuList}>
        <li
          className={`${styles.menuItem} ${selectedMenu === "info" ? styles.active : ""}`}
          onClick={() => onSelectMenu("info")}
        >
          Thông tin cơ bản
        </li>
        <li
          className={`${styles.menuItem} ${selectedMenu === "changePassword" ? styles.active : ""}`}
          onClick={() => onSelectMenu("changePassword")}
        >
          Đổi mật khẩu
        </li>
        <li 
          className={`${styles.menuItem} ${selectedMenu === "changeAvatar" ? styles.active : ""}`}
          onClick={() => onSelectMenu("changeAvatar")}
        >
            Cập nhật hình đại diện
        </li>
        <li className={styles.menuItem}>Quản lý địa chỉ</li>
        <li 
        className={`${styles.menuItem} ${selectedMenu === "logout" ? styles.active : ""}`}
          onClick={() => onSelectMenu("logout")}
        >
            Đăng xuất
        </li>
      </ul>
    </div>
  );
}
