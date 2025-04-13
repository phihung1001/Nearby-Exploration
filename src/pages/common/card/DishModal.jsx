import React, { useState } from 'react';
import styles from './DishModal.module.css';
import { notification, Spin } from 'antd';
import { useNavigate, useLocation } from "react-router-dom"; 
import ItemRestaurant from '../card/ItemRestaurant'; // Đường dẫn đến ItemRestaurant
import { getSearchKeywords } from '../../../untils/keywordUtils';

export default function DishModal({ isOpen, onClose, data }) {
  const [activeIndex, setActiveIndex] = useState(null);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [userLocation, setUserLocation] = useState(null);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  if (!isOpen) return null;

    // Hàm chuyển đổi URL ảnh lỗi
    const fixImageUrl = (url) => {
      if (url.startsWith("https://images.foody.vn/")) {
        const parts = url.split('/').pop();
        return `https://down-tx-vn.img.susercontent.com/${parts}`;
      }
      return url;
    };

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
    if (loading || activeIndex === null) return;
      setLoading(true);
    try {
      const dishName = data.dishes[activeIndex]?.name; 
      console.log("dishName:",dishName)
      const searchKeywords = getSearchKeywords(dishName);
      console.log("searchKeywords:",searchKeywords)

      let allRestaurants = [];

      for (const keyword of searchKeywords) {
        const response = await fetch(
          `http://localhost:8080/public/restaurant/nearby?name=${encodeURIComponent(keyword)}&latitude=${latitude}&longitude=${longitude}`
        );

        if (!response.ok) {
          throw new Error(`API lỗi: ${response.status}`);
        }

        const result = await response.json();
        if (Array.isArray(result.content) && result.content.length > 0) {
          allRestaurants = [...allRestaurants, ...result.content];
        }
      }

      if (allRestaurants.length === 0) {
        throw new Error("Không tìm thấy nhà hàng phù hợp");
      }

      setRestaurants(allRestaurants);
      notification.success({
        message: 'Đề xuất thành công',
        description: 'Nhà hàng gần bạn đã được tìm thấy.',
      });
    } catch (error) {
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
                {restaurants.map((r, index) => (
                  <ItemRestaurant
                    key={`${r.id}-${index}`} 
                    name={r.name}
                    address={r.address}
                    latestComment={r.latestComment}
                    reviewCount={r.totalReviews}
                    imageCount={r.totalPictures}
                    rating={r.avgRatingText}
                    image={fixImageUrl(r.photoUrl)}
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
