import React, { useEffect, useState } from "react";
import { Card, Button, Row, Col } from "antd";
import { useParams } from "react-router-dom";
import styles from "./RestaurantMenu.module.css"; // Import CSS module
import FoodItem from "../food-item/FoodItem";
const RestaurantMenu = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [nameRes, setNameRes] = useState(null);
  const { id } = useParams(); // Lấy ID từ URL
    const [loading, setLoading] = useState(true);
  
  useEffect(() => {
      // Fetch dữ liệu từ API
      fetch("/restaurants.json")
        .then((res) => res.json())
        .then((data) => {
          const foundRestaurant = data.find((r) => r.id.toString() === id);
          if (foundRestaurant) {
            setNameRes(foundRestaurant.name);
            setMenuItems(foundRestaurant.menu);
          } else {
            console.error("Không tìm thấy nhà hàng");
          }
        })
        .catch((error) => console.error("Lỗi tải dữ liệu:", error))
        .finally(() => setLoading(false)); // Dừng trạng thái loading
   
    }, [id]);
    console.log('menuItems',menuItems)
    if (loading) return <p>Đang tải...</p>;

  return (
    <div className={styles.menuContainer}>
        {menuItems.map((item) => (
      
              <div className={styles.foodInfo}>
                <FoodItem 
                    nameRestaurant={nameRes}
                    image={item.image} 
                    name={item.name}
                    price={item.price}
                />

              </div>
        ))}
      <div className={styles.orderButtonContainer}>
        <Button type="primary" size="large" className={styles.orderButton}>
          Xem thêm 
        </Button>
      </div>
    </div>
  );
};

export default RestaurantMenu;
