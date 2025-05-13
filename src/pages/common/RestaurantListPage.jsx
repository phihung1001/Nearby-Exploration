import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { notification, Spin, Empty } from "antd";
import ItemRestaurant from "../common/card/ItemRestaurant";
import styles from "../common/home-body/Restaurant.module.css";
import Header from "../../components/header/Header";
import queryString from "query-string";

export default function RestaurantListPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const params = queryString.parse(location.search);

  const [restaurants, setRestaurants] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  // Đổi URL ảnh lỗi (nếu cần)
  const fixImageUrl = (url) => {
    if (url?.startsWith("https://images.foody.vn/")) {
      const parts = url.split("/").pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  const fetchRestaurants = async () => {
    if (loading || !hasMore) return;
    setLoading(true);

    const query = new URLSearchParams({
      ...params,
      page,
    }).toString();
    
    try {
      const res = await fetch(`http://localhost:8080/public/restaurant/filter?${query}`);
      const data = await res.json();
      setRestaurants((prev) => {
        const existingIds = new Set(prev.map(r => r.id));
        const newRestaurants = data.content.filter(r => !existingIds.has(r.id));
        return [...prev, ...newRestaurants];
      });      setHasMore(!data.last);
    } catch (error) {
      notification.error({
        message:"Thất bại",
        description:"Lỗi khi gọi API:", error
      });
    } finally {
      setLoading(false);
    }
  };

  // Khi query thay đổi → reset danh sách
  useEffect(() => {
    setRestaurants([]);
    setPage(0);
    setHasMore(true);
  }, [location.search]);

  // Gọi API khi page thay đổi
  useEffect(() => {
    fetchRestaurants();
  }, [page, location.search]);

  // Cuộn để tải thêm
  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 &&
        hasMore &&
        !loading
      ) {
        setPage((prev) => prev + 1);
      }
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasMore, loading]);

  const handleClick = (restaurant) => {
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  const handleSave = async (restaurant) => {
    try {
    const token = localStorage.getItem("token");
      const response = await fetch(`http://localhost:8080/public/restaurant/save/${restaurant.id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(restaurant)
      });
      const data = await response.json();
      if (!response.ok) {
         throw new Error(`${data.message}`);
      }
      notification.success({
        message: "Lưu thành công",
        description: `Nhà hàng "${restaurant.name}" đã được lưu vào mục yêu thích của bạn`,
      });
    } 
    catch(err) {
      notification.error({
        message: "Lỗi khi lưu",
        description: err.message,
      });
    }
  };

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.productContainer}>
        {restaurants.map((r) => (
          <ItemRestaurant
            key={`${r.id}-${r.name}`}
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
      </div>
      {loading && <Spin style={{ display: "block", marginTop: 16 }} />}
      {!loading && restaurants.length === 0 && <Empty description="Không tìm thấy nhà hàng" />}
      {!hasMore && restaurants.length > 0 && (
        <p style={{ textAlign: "center", marginTop: 16 }}>Đã tải hết nhà hàng.</p>
      )}
    </div>
  );
}
