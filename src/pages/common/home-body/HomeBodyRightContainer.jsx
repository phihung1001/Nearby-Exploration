import React, { useState } from "react";
import styles from "./HomeBodyRightContainer.module.css";
import RestaurantList from "./RestaurantList";
// Hoặc bạn có thể import ItemRestaurant và render trực tiếp

export default function HomeBodyRightContainer() {
  // State quản lý tab đang chọn
  const [activeTab, setActiveTab] = useState("trangchu");

  // Xử lý khi nhấn vào từng nút
  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  return (
    <div className={styles.rightContainer}>
      {/* Header của phần homeBodyRightContainer */}
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
        {activeTab === "trangchu" && (
          <div>
            {/* Gọi danh sách nhà hàng, mặc định Trang chủ hiển thị list nhà hàng */}
            <RestaurantList />
          </div>
        )}
        {activeTab === "giamgia" && (
          <div>
            <p>Danh sách nhà hàng đang giảm giá...</p>
          </div>
        )}
        {activeTab === "hot" && (
          <div>
            <p>Danh sách nhà hàng "Hot"...</p>
          </div>
        )}
        {activeTab === "gantoi" && (
          <div>
            <p>Danh sách nhà hàng gần tôi...</p>
          </div>
        )}
        {activeTab === "daluu" && (
          <div>
            <p>Danh sách nhà hàng đã lưu...</p>
          </div>
        )}
      </div>
    </div>
  );
}
