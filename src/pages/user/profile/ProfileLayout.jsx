import React from "react";
import ProfileMenu from "./ProfileMenu";
import styles from "./Profile.module.css";

export default function ProfileLayout({ userData, selectedMenu, onSelectMenu, rightContent }) {
  return (
    <div className={styles.profileContainer}>
      {/* Menu bên trái */}
      <ProfileMenu
        userData={userData}
        selectedMenu={selectedMenu}
        onSelectMenu={onSelectMenu}
      />

      {/* Nội dung bên phải */}
      <div className={styles.rightPanel}>
        {rightContent}
      </div>
    </div>
  );
}
