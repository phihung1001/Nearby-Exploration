import React, { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { notification } from "antd";
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";
import { fetchNearbyRestaurantsAPI, saveRestaurant } from "../../../services/restaurantService";
import { getCurrentLocation } from "../../../utils/geolocation";

export default function NearbyRestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [location, setLocation] = useState(null);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observerRef = useRef(null);
  const navigate = useNavigate();

  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split("/").pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  useEffect(() => {
    getCurrentLocation()
      .then(setLocation)
      .catch(() => {
        notification.error({
          message: "Thất bại",
          description: "Không thể lấy vị trí của bạn.",
        });
      });
  }, []);

  const fetchNearbyRestaurants = useCallback(async () => {
    if (loading || !hasMore || !location) return;
    setLoading(true);

    try {
      const data = await fetchNearbyRestaurantsAPI({ latitude: location.latitude, longitude: location.longitude, page });

      if (data.content?.length > 0) {
        setRestaurants((prev) => [...prev, ...data.content]);
        setPage((prev) => prev + 1);
      } else {
        setHasMore(false);
      }
    } catch (error) {
      notification.error({
        message: "Lỗi",
        description: error.message,
      });
    } finally {
      setLoading(false);
    }
  }, [location, page, loading, hasMore]);

  useEffect(() => {
    if (location) {
      fetchNearbyRestaurants();
    }
  }, [location]);

  useEffect(() => {
    const target = document.querySelector("#load-more");
    if (!target || !hasMore || loading) return;

    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        fetchNearbyRestaurants();
      }
    });

    observer.observe(target);
    observerRef.current = observer;

    return () => observer.disconnect();
  }, [fetchNearbyRestaurants, loading, hasMore]);

  const handleClick = (restaurant) => {
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  const handleSave = async (restaurant) => {
    try {
      const token = localStorage.getItem("token");
      await saveRestaurant(restaurant, token);
      notification.success({
        message: "Lưu thành công",
        description: `Nhà hàng "${restaurant.name}" đã được lưu.`,
      });
    } catch (error) {
      notification.error({
        message: "Lỗi khi lưu",
        description: error.message,
      });
    }
  };

  return (
    <div className={styles.productContainer}>
      {restaurants.map((r) => (
        <ItemRestaurant
          key={r.id}
          name={r.name}
          address={r.address}
          latestComment={r.latestComment}
          reviewCount={r.totalReviews}
          imageCount={r.totalPictures}
          rating={r.avgRatingText}
          image={fixImageUrl(r.photoUrl)}
          onClick={() => handleClick(r)}
          onSave={() => handleSave(r)}
        />
      ))}
      {loading && <div className={styles.loadingSpinner}></div>}
      {!hasMore && <p>Đã tải hết dữ liệu.</p>}
      <div id="load-more" style={{ height: "20px" }}></div>
    </div>
  );
}
