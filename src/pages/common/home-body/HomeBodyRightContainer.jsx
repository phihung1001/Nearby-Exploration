import React, { useState } from "react";
import styles from "./HomeBodyRightContainer.module.css";
import RestaurantList from "./RestaurantList";
import NearbyRestaurantList from "./NearbyRestaurantList"; // Import component mới
import SavedRestaurantList from "./SavedRestaurantList"; // Import component mới
export default function HomeBodyRightContainer() {
  // State quản lý tab đang chọn
  const [activeTab, setActiveTab] = useState("trangchu");

  // Xử lý khi nhấn vào từng nút
  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  return (
    <div className={styles.rightContainer}>
      {/* Header của phần HomeBodyRightContainer */}
      <div className={styles.headerTabs}>
        <button
          className={activeTab === "trangchu" ? styles.active : ""}
          onClick={() => handleTabClick("trangchu")}
        >
          Trang chủ
        </button>
        <button
          className={activeTab === "giamgia" ? styles.active : ""}
          onClick={() => handleTabClick("giamgia")}
        >
          Giảm giá
        </button>
        <button
          className={activeTab === "hot" ? styles.active : ""}
          onClick={() => handleTabClick("hot")}
        >
          Hot
        </button>
        <button
          className={activeTab === "gantoi" ? styles.active : ""}
          onClick={() => handleTabClick("gantoi")}
        >
          Gần tôi
        </button>
        <button
          className={activeTab === "daluu" ? styles.active : ""}
          onClick={() => handleTabClick("daluu")}
        >
          Đã lưu
        </button>
      </div>

      {/* Nội dung hiển thị theo tab */}
      <div className={styles.content}>
        {activeTab === "trangchu" && <RestaurantList />}
        {activeTab === "giamgia" && <p>Danh sách nhà hàng đang giảm giá...</p>}
        {activeTab === "hot" && <p>Danh sách nhà hàng "Hot"...</p>}
        {activeTab === "gantoi" && <NearbyRestaurantList />}
        {activeTab === "daluu" &&  <SavedRestaurantList />}
      </div>
    </div>
  );
}
