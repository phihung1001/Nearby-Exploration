import React, { useState } from 'react';
import styles from './DishModal.module.css';
import { notification, Spin } from 'antd';
import { useNavigate, useLocation } from "react-router-dom"; 
import ItemRestaurant from '../card/ItemRestaurant'; // Đường dẫn đến ItemRestaurant
export default function DishModal({ isOpen, onClose, data }) {
  const [activeIndex, setActiveIndex] = useState(null);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [userLocation, setUserLocation] = useState(null);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  if (!isOpen) return null;

  const handleToggle = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
    setRestaurants([]); // Reset danh sách nhà hàng khi chọn món khác
  };

  const getUserLocation = () => {
    if (!navigator.geolocation) {
      notification.error({
        message: 'Lỗi',
        description: 'Trình duyệt của bạn không hỗ trợ định vị.',
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        setUserLocation({ latitude, longitude });
        fetchRestaurants(latitude, longitude); // Gọi API ngay sau khi có tọa độ
      },
      (error) => {
        console.error('Lỗi lấy vị trí:', error);
        notification.error({
          message: 'Lỗi lấy vị trí',
          description: 'Không thể lấy vị trí hiện tại của bạn.',
        });
      }
    );
  };

  const fetchRestaurants = async (latitude, longitude) => {
    if (loading || activeIndex === null) return; // Tránh gọi API liên tục
  
    console.log('Fetching restaurants...', latitude, longitude);
    setLoading(true);
    try {
      const dishName = data.dishes[activeIndex]?.name; // Kiểm tra có dữ liệu không
      const response = await fetch(
        `http://localhost:8080/public/restaurant/nearby?name=${encodeURIComponent(dishName)}&latitude=${latitude}&longitude=${longitude}`
      );
  
      if (!response.ok) {
        throw new Error(`API lỗi: ${response.status}`);
      }
  
      const restaurants = await response.json();
      if (!Array.isArray(restaurants)) {
        throw new Error("API không trả về danh sách hợp lệ");
      }
  
      setRestaurants(restaurants);
      console.log('Restaurants:', restaurants);
      notification.success({
        message: 'Đề xuất thành công',
        description: 'Nhà hàng gần bạn đã được tìm thấy.',
      });
    } catch (error) {
      console.error('Lỗi lấy danh sách nhà hàng:', error);
      notification.error({
        message: 'Lỗi',
        description: 'Không thể lấy danh sách nhà hàng. Vui lòng thử lại sau.',
      });
    }
    setLoading(false);
  };
  
  const handleClick = (restaurant) => {
    console.log("restaurant-detail", restaurant);
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  return (
    <div className={styles.modalContentDishModal}>
      <div className={styles.modalOverlayDishModal}>
        <h2>{data.title}</h2>
        <p className={styles.description}>{data.description}</p>

        <div className={styles.dishList}>
          {data.dishes.map((dish, index) => (
            <button
              key={index}
              className={`${styles.dishButton} ${activeIndex === index ? styles.active : ''}`}
              onClick={() => handleToggle(index)}
            >
              {dish.name}
            </button>
          ))}
        </div>

        {activeIndex !== null && (
          <div className={styles.dishDetails}>
            <h3>{data.dishes[activeIndex].name}</h3>
            <p>{data.dishes[activeIndex].description}</p>
            <button className={styles.dishButton} onClick={getUserLocation}>
              Đề xuất nhà hàng gần tôi.
            </button>
          </div>
        )}

         {/* Hiển thị danh sách nhà hàng */}
         {loading && <Spin />}
         <div className="restaurantList">
          {restaurants.length > 0 && (
            <div className={styles.restaurantList}>
              <h3>Nhà hàng gần bạn </h3>
                {restaurants.map((r) => (
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
                ))}
            </div>
          )}
          </div>
      </div>
    </div>
  );
}
