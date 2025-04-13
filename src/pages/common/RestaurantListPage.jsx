import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Typography, Input, Row, Col, Card, Empty } from "antd";
import ItemRestaurant from "../common/card/ItemRestaurant"; 
import styles from "../common/home-body/Restaurant.module.css"; 
import Header from "../../components/header/Header";
const { Title } = Typography;
const { Search } = Input;

export default function RestaurantListPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const restaurants = location.state?.restaurants || [];

  console.log("restaurants list page", restaurants);
  
  // Hàm chuyển đổi URL ảnh lỗi
  const fixImageUrl = (url) => {
    if (url.startsWith("https://images.foody.vn/")) {
      const parts = url.split('/').pop();
      return `https://down-tx-vn.img.susercontent.com/${parts}`;
    }
    return url;
  };

  // Xử lý khi nhấp vào nhà hàng
  const handleClick = (restaurant) => {
    console.log("restaurant-detail", restaurant);
    navigate(`/public/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };

  // Xử lý tìm kiếm nhà hàng
  const handleSearch = (value) => {
    console.log("Tìm kiếm:", value);
    // Thực hiện tìm kiếm nhà hàng tại đây
  };

  return (
    <div className={styles.container}>
      <Header />      
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
    </div>
    </div>
  );
}