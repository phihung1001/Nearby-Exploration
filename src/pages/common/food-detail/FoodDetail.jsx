import React, { useEffect, useState } from "react";
import { useParams, useLocation } from "react-router-dom";
import { Card, Rate, Button } from "antd";
import styles from "./FoodDetail.module.css";

const FoodDetail = () => {
  const { restaurantId, foodId } = useParams(); // Lấy ID từ URL
  const location = useLocation();
  const food = location.state; // Lấy dữ liệu từ navigate()

  if (!food) {
    return <p>Không tìm thấy món ăn!</p>;
  }

  return (
    <Card className={styles.foodCard}>
      <img src={food.image} alt={food.name} className={styles.foodImage} />
      <div className={styles.foodInfo}>
        <h2 className={styles.foodName}>{food.name}</h2>
        <p className={styles.foodDescription}>{food.description}</p>
        <div className={styles.foodPrice}>{food.price}₫</div>
        <Rate disabled defaultValue={food.rating} className={styles.foodRating} />
        <Button type="primary" className={styles.addToCartBtn}>
          Thêm vào giỏ hàng
        </Button>
      </div>
    </Card>
  );
};

export default FoodDetail;
