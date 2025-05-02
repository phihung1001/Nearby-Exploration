import React, { useState, useEffect, useCallback, useRef } from "react";
import { notification } from "antd";
import { useNavigate } from "react-router-dom";
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";
import { getSavedRestaurants, saveRestaurant } from "../../../services/restaurantService";

// Debounce helper
function debounce(func, delay) {
  let timer;
  return (...args) => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => func(...args), delay);
  };
}

// Image URL fallback
const fixImageUrl = (url) => {
  if (url.startsWith("https://images.foody.vn/")) {
    const parts = url.split('/').pop();
    return `https://down-tx-vn.img.susercontent.com/${parts}`;
  }
  return url;
};

export default function SavedRestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const initialLoadDone = useRef(false);
  const navigate = useNavigate();

  const fetchRestaurants = useCallback(async () => {
    if (loading || !hasMore) return;

    setLoading(true);
    const token = localStorage.getItem("token");

    try {
      const data = await getSavedRestaurants(page, token);

      if (data.content?.length) {
        setRestaurants((prev) => {
          const newItems = data.content.filter(
            (item) => !prev.some((r) => r.id === item.id)
          );
          return [...prev, ...newItems];
        });
        setPage((prev) => prev + 1);
      } else {
        setHasMore(false);
      }
    } catch (error) {
      notification.error({
        message: "Lỗi khi lấy dữ liệu",
        description: error.message,
      });
    } finally {
      setLoading(false);
      initialLoadDone.current = true;
    }
  }, [loading, hasMore, page]);

  useEffect(() => {
    fetchRestaurants();
  }, []);

  useEffect(() => {
    const handleScroll = debounce(() => {
      if (
        window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 200 &&
        initialLoadDone.current
      ) {
        fetchRestaurants();
      }
    }, 200);

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [fetchRestaurants]);

  const handleSave = async (restaurant) => {
    const token = localStorage.getItem("token");
    try {
      await saveRestaurant(restaurant, token);
      notification.success({
        message: "Lưu thành công",
        description: `Nhà hàng "${restaurant.name}" đã được lưu.`,
      });
    } catch (err) {
      notification.error({
        message: "Lỗi khi lưu",
        description: err.message,
      });
    }
  };

  const handleClick = (restaurant) => {
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  return (
    <div className={styles.productContainer}>
      {restaurants.map((restaurant) => (
        <ItemRestaurant
          key={restaurant.id}
          name={restaurant.name}
          address={restaurant.address}
          latestComment={restaurant.latestComment}
          reviewCount={restaurant.totalReviews}
          imageCount={restaurant.totalPictures}
          rating={restaurant.avgRatingText}
          image={fixImageUrl(restaurant.photoUrl)}
          onSave={() => handleSave(restaurant)}
          onClick={() => handleClick(restaurant)}
        />
      ))}
      {loading && <div className={styles.loadingSpinner}></div>}
      {!hasMore && !loading && <p>Đã tải hết dữ liệu.</p>}
    </div>
  );
}
