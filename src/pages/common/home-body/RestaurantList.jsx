import React, { useState, useEffect } from "react";
import ItemRestaurant from "../card/ItemRestaurant";

export default function RestaurantList() {
  // Dữ liệu nhà hàng giả lập
  const [restaurants, setRestaurants] = useState([]);
  useEffect(() => {
    fetch("/restaurants.json") 
      .then((res) => res.json())
      .then((data) => setRestaurants(data))
      .catch((error) => console.error("Error loading JSON:", error));
  }, []);
  
  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
      {restaurants.map((r, idx) => (
        <ItemRestaurant
          key={idx}
          name={r.name}
          address={r.address}
          latestComment={r.latestComment}
          reviewCount={r.reviewCount}
          imageCount={r.imageCount}
          rating={r.rating}
          image={r.image}
        />
      ))}
    </div>
  );
}
