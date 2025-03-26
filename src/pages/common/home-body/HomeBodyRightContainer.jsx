import React, { useState, useEffect } from "react";
import styles from "./HomeBodyRightContainer.module.css";
import RestaurantList from "./RestaurantList";
import ItemRestaurant from "../card/ItemRestaurant";
import styles2 from "./Restaurant.module.css";
import { useNavigate } from "react-router-dom"; 

export default function HomeBodyRightContainer() {
  // State quản lý tab đang chọn
  const [activeTab, setActiveTab] = useState("trangchu");
  const [nearbyRestaurants, setNearbyRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); 

  // Xử lý khi nhấn vào từng nút
  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
    if (tabName === "gantoi") {
      fetchNearbyRestaurants();
    }
  };

  // Hàm gọi API lấy danh sách nhà hàng gần vị trí hiện tại
  const fetchNearbyRestaurants = async () => {
    setLoading(true);
    try {
      // Lấy tọa độ hiện tại của người dùng
      navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords;
        const response = await fetch(`http://localhost:8080/public/restaurant/nearby?latitude=${latitude}&longitude=${longitude}&radius=5`);
        const data = await response.json();
        setNearbyRestaurants(data);
        setLoading(false);
      }, (error) => {
        console.error("Error getting location:", error);
        setLoading(false);
      });
    } catch (error) {
      console.error("Error fetching nearby restaurants:", error);
      setLoading(false);
    }
  };

  const handleClick = (restaurant) => {
    console.log("restaurant-detail", restaurant);
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
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
            {loading ? (
              <p>Loading...</p>
            ) : (
              <div className={styles2.productContainer}>
                {nearbyRestaurants.length > 0 ? (
                  nearbyRestaurants.map((r) => (
                    <ItemRestaurant
                      key={r.id}
                      name={r.name}
                      address={r.address}
                      latestComment={r.latestComment}
                      reviewCount={r.totalReviews}
                      imageCount={r.totalPictures}
                      rating={r.avgRatingText}
                      image={r.photoUrl}
                      onClick={() => handleClick(r)}
                    />
                  ))
                ) : (
                  <p>Không có nhà hàng nào gần bạn.</p>
                )}
              </div>
            )}
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