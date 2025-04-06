import React, { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom"; 
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";

// Hàm debounce để giới hạn số lần gọi hàm
function debounce(func, delay) {
  let timer;
  return (...args) => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      func(...args);
    }, delay);
  };
}

export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const initialLoadDone = useRef(false); // Đánh dấu lần load đầu tiên

  // Hàm chuyển đổi URL ảnh lỗi
  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split('/').pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  // Fetch dữ liệu từ API
  const fetchRestaurants = useCallback(async () => {
    if (loading || !hasMore) return;
  
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/public/restaurant/list?page=${page}`, {
        method: "GET",
      });
      const data = await response.json();
  
      if (data.content && data.content.length > 0) {
        setRestaurants((prev) => [...prev, ...data.content]);
        setPage((prevPage) => prevPage + 1);
      } else {
        setHasMore(false);
      }
    } catch (error) {
      console.error("Error loading restaurant data:", error);
    } finally {
      setLoading(false);
      initialLoadDone.current = true;
    }
  }, [page, loading, hasMore]);

  // Load trang đầu tiên khi component mount
  useEffect(() => {
    fetchRestaurants();
  }, []);

  // Xử lý cuộn trang để tải thêm dữ liệu (Infinite Scroll) với debounce
  useEffect(() => {
    const handleScroll = debounce(() => {
      // Nếu nội dung trang chưa đủ cao, có thể scroll luôn ở vị trí đầu -> tránh gọi lại ngay sau khi mount
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 200 &&
        initialLoadDone.current
      ) {
        fetchRestaurants();
      }
    }, 200);

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [fetchRestaurants]);

  // Xử lý khi nhấp vào nhà hàng
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
      {loading && <p>Loading...</p>}
      {!hasMore && <p>Đã tải hết dữ liệu.</p>}
    </div>
  );
}
