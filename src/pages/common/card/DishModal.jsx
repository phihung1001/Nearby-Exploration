import React, { useState } from 'react';
import styles from './DishModal.module.css';
import { notification, Spin } from 'antd';
import { useNavigate } from "react-router-dom";
import ItemRestaurant from '../card/ItemRestaurant';
import { getSearchKeywords } from '../../../utils/keywordUtils';
import { searchNearbyRestaurantsByKeywords } from '../../../services/restaurantService';

export default function DishModal({ isOpen, onClose, data }) {
  const [activeIndex, setActiveIndex] = useState(null);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  if (!isOpen) return null;

  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split('/').pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  const handleToggle = (index) => {
    setActiveIndex(index === activeIndex ? null : index);
    setRestaurants([]);
  };

  const handleSuggestRestaurants = () => {
    if (!navigator.geolocation) {
      notification.error({
        message: 'Không hỗ trợ định vị',
        description: 'Trình duyệt không hỗ trợ định vị.',
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords;
        await fetchRestaurants(latitude, longitude);
      },
      (error) => {
        notification.error({
          message: 'Lỗi lấy vị trí',
          description: 'Không thể lấy vị trí hiện tại của bạn.',
        });
      }
    );
  };

  const fetchRestaurants = async (latitude, longitude) => {
    if (activeIndex === null || loading) return;
    setLoading(true);
    try {
      const dishName = data.dishes[activeIndex].name;
      const keywords = getSearchKeywords(dishName);

      const results = await searchNearbyRestaurantsByKeywords(keywords, latitude, longitude);
      if (results.length === 0) throw new Error("Không tìm thấy nhà hàng phù hợp.");

      setRestaurants(results);
      notification.success({
        message: 'Đề xuất thành công',
        description: 'Đã tìm thấy nhà hàng gần bạn.',
      });
    } catch (error) {
      notification.error({
        message: 'Lỗi',
        description: error.message || 'Không thể lấy danh sách nhà hàng.',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleClick = (restaurant) => {
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
            <button className={styles.dishButton} onClick={handleSuggestRestaurants}>
              Đề xuất nhà hàng gần tôi
            </button>
          </div>
        )}

        {loading && <Spin style={{ marginTop: 20 }} />}

        {restaurants.length > 0 && (
          <div className={styles.restaurantList}>
            <h3>Nhà hàng gần bạn</h3>
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
  );
}
