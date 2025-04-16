import React, { useState, useEffect, useCallback, useRef } from "react";
import { notification } from "antd";
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";
import { useNavigate } from "react-router-dom";

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

export default function SavedRestaurantList() {
  const [savedRestaurants, setSavedRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(0);
  const initialLoadDone = useRef(false); // Đánh dấu lần load đầu tiên
  const navigate = useNavigate();

  // Hàm chuyển đổi URL ảnh lỗi
  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split('/').pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  // Fetch danh sách nhà hàng đã lưu
  const fetchSavedRestaurants = useCallback(async () => {
    if (loading || !hasMore) return;

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/customer/restaurant-save/list?page=${page}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      if (response.ok) {
        if (data.content && data.content.length > 0) {
          // Chỉ cập nhật nếu có dữ liệu mới và tránh gọi lại quá nhiều lần
          setSavedRestaurants((prev) => {
            const newRestaurants = data.content.filter(
              (newRestaurant) => !prev.some((restaurant) => restaurant.id === newRestaurant.id)
            );
            return [...prev, ...newRestaurants];
          });
          setPage((prevPage) => prevPage + 1);
        } else {
          setHasMore(false); // Không còn dữ liệu để tải
        }
      } else {
        notification.error({
          message: "Lỗi khi lấy dữ liệu",
          description: data.message || "Có lỗi xảy ra khi lấy danh sách nhà hàng đã lưu.",
        });
      }
    } catch (error) {
      notification.error({
        message: "Lỗi kết nối",
        description: "Không thể kết nối đến máy chủ.",
      });
    } finally {
      setLoading(false);
      initialLoadDone.current = true;
    }
  }, [loading, page, hasMore]);

  // Load trang đầu tiên khi component mount
  useEffect(() => {
    fetchSavedRestaurants();
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
        fetchSavedRestaurants();
      }
    }, 200);

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [fetchSavedRestaurants]);

  // Hàm lưu nhà hàng
  const handleSave = async (restaurant) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/public/restaurant/save/${restaurant.id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(restaurant),
      });
      const data = await response.json();
      if (!response.ok) {
        throw new Error(`${data.message}`);
      }
      notification.success({
        message: "Lưu thành công",
        description: `Nhà hàng "${restaurant.name}" đã được lưu vào mục yêu thích của bạn`,
      });
    } catch (err) {
      notification.error({
        message: "Lỗi khi lưu",
        description: err.message,
      });
    }
  };

  // Xử lý khi nhấp vào nhà hàng
  const handleClick = (restaurant) => {
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  return (
    <div className={styles.productContainer}>
      {savedRestaurants.map((restaurant) => (
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
