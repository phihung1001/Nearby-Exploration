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
          src={userData?.avatar || null}
          icon={!userData?.avatar && <UserOutlined />}
          className={styles.avatar}
        />
        <p>{userData?.fullName || "Người dùng"}</p>
      </div>

      <ul className={styles.menuList}>
        <li
          className={`${styles.menuItem} ${selectedMenu.menu === "info" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "info" })}
        >
          Thông tin cơ bản
        </li>
        <li
          className={`${styles.menuItem} ${selectedMenu.menu === "changePassword" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "changePassword" })}
        >
          Đổi mật khẩu
        </li>
        <li 
          className={`${styles.menuItem} ${selectedMenu.menu === "changeAvatar" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "changeAvatar" })}
        >
          Cập nhật hình đại diện
        </li>
        <li 
          className={`${styles.menuItem} ${selectedMenu.menu === "restaurantManagement" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "restaurantManagement" })}
        >
          Quản lí nhà hàng
        </li>
        <li 
          className={`${styles.menuItem} ${selectedMenu.menu === "dishesManagement" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "dishesManagement" })}
        >
          Quản lí món ăn
        </li>
        <li 
          className={`${styles.menuItem} ${selectedMenu.menu === "logout" ? styles.active : ""}`}
          onClick={() => onSelectMenu({ menu: "logout" })}
        >
          Đăng xuất
        </li>
      </ul>
    </div>
  );
}
