import React, { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";

export default function NearbyRestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [location, setLocation] = useState(null);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observer = useRef();
  const navigate = useNavigate();

  // Hàm chuyển đổi URL ảnh lỗi
  const fixImageUrl = (url) => {
  if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split('/').pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
  }
  return url;
  };

  // Lấy vị trí người dùng khi component mount
  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setLocation({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
      },
      (error) => {
        console.error("Lỗi khi lấy vị trí:", error);
      }
    );
  }, []);

  // Hàm fetch API lấy danh sách nhà hàng gần bạn với phân trang
  const fetchNearbyRestaurants = useCallback(async () => {
    if (loading || !hasMore || !location) return;
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/public/restaurant/nearby?latitude=${location.latitude}&longitude=${location.longitude}&page=${page}`
      );
      const data = await response.json();
      console.log("Dữ liệu nhà hàng gần bạn:", data);
      if (data && data.content.length > 0) {
        setRestaurants((prev) => [...prev, ...data.content]);
        setPage((prevPage) => prevPage + 1);
        setHasMore(true);
      } else {
        setHasMore(false);
      }
    } catch (error) {
      console.error("Lỗi khi tải nhà hàng:", error);
    } finally {
      setLoading(false);
    }
  }, [location, page, loading, hasMore]);

  // Khi có vị trí, fetch trang đầu tiên
  useEffect(() => {
    if (location) {
      fetchNearbyRestaurants();
    }
  }, [location]);

  // Xử lý cuộn trang bằng Intersection Observer
  useEffect(() => {
    if (!loading && hasMore) {
      const handleObserver = (entries) => {
        const target = entries[0];
        if (target.isIntersecting) {
          fetchNearbyRestaurants();
        }
      };

      observer.current = new IntersectionObserver(handleObserver);
      if (observer.current && observer.current.observe) {
        observer.current.observe(document.querySelector("#load-more"));
      }
    }

    return () => observer.current && observer.current.disconnect();
  }, [loading, hasMore, fetchNearbyRestaurants]);

  const handleClick = (restaurant) => {
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  return (
    <div className={styles.productContainer}>
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
      {loading && <p>Đang tải...</p>}
      {!hasMore && <p>Đã tải hết dữ liệu.</p>}
      <div id="load-more" style={{ height: "20px" }}></div>
    </div>
  );
}
