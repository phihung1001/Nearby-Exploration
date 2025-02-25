import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate
import ItemRestaurant from "../card/ItemRestaurant";
import styles from "./Restaurant.module.css"
export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const navigate = useNavigate(); 

  useEffect(() => {
    fetch("/restaurants.json")
      .then((res) => res.json())
      .then((data) => setRestaurants(data))
      .catch((error) => console.error("Error loading JSON:", error));
  }, []);

  // Xử lý khi nhấp vào nhà hàng
  const handleClick = (restaurant) => {
    console.log("restaurant-detail", restaurant);
    navigate(`/restaurant-detail/${restaurant.id}`, { state: restaurant });
  };
  

  return (
    <div className={styles.productContainer}>
      {restaurants.map((r) => (
        <ItemRestaurant
          key={r.id}
          name={r.name}
          address={r.address}
          latestComment={r.latestComment}
          reviewCount={r.reviewCount}
          imageCount={r.imageCount}
          rating={r.rating}
          image={r.image[0]}
          onClick={() => handleClick(r)}
        />
      ))}
    </div>
  );
}
