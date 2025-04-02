import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css";

export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true); // Kiểm tra còn dữ liệu hay không
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); 
  // Hàm chuyển đổi URL ảnh lỗi
  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
        const parts = url.split('/').pop(); // Lấy phần cuối của URL
        return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };
  // Fetch dữ liệu từ API
  const fetchRestaurants = async (currentPage) => {
    if (loading || !hasMore) return;
  
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/public/restaurant/list?page=${currentPage}`, {
        method: "GET",
      });
  
      const data = await response.json();
  
      if (data.content && data.content.length > 0) {
        setRestaurants((prev) => [...prev, ...data.content]);
        setPage(currentPage + 1); // Cập nhật page ngay tại đây
      } else {
        setHasMore(false); // Hết dữ liệu
      }
    } catch (error) {
      console.error("Error loading restaurant data:", error);
    } finally {
      setLoading(false);
    }
  };
  
  // Load trang đầu tiên khi render
  useEffect(() => {
    if (page >= 0 && hasMore) {
        fetchRestaurants(page);
    }
}, [page]);
  // Xử lý cuộn trang để tải thêm dữ liệu (Infinite Scroll)
  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 200
      ) {
        fetchRestaurants(page); // Sử dụng giá trị page mới nhất
      }
    };
  
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [page, hasMore]);
  

  // Xử lý khi nhấp vào nhà hàng
  const handleClick = (restaurant) => {
    console.log("restaurant-detail", restaurant);
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
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
        />
      ))}

      {loading && <p>Loading...</p>}
      {!hasMore && <p>Đã tải hết dữ liệu.</p>}
    </div>
  );
}
