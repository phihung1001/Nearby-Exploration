import React, { useEffect, useState } from "react";
import { Button } from "antd";
import { useParams ,useNavigate} from "react-router-dom";
import styles from "./RestaurantMenu.module.css"; // Import CSS module
import FoodItem from "../food-item/FoodItem";
const RestaurantMenu = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [nameRes, setNameRes] = useState(null);
  const navigate = useNavigate(); 
  const { id: restaurantId } = useParams();
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
      // Fetch dữ liệu từ API
      fetch("/restaurants.json")
        .then((res) => res.json())
        .then((data) => {
          const foundRestaurant = data.find((r) => r.id.toString() === restaurantId);
          if (foundRestaurant) {
            setNameRes(foundRestaurant.name);
            setMenuItems(foundRestaurant.menu);
          } else {
            console.error("Không tìm thấy nhà hàng");
          }
        })
        .catch((error) => console.error("Lỗi tải dữ liệu:", error))
        .finally(() => setLoading(false)); // Dừng trạng thái loading
   
    }, [restaurantId]);
    console.log('menuItems',menuItems)
    if (loading) return <p>Đang tải...</p>;

  
    // Xử lý khi nhấp vào mon an
    const handleClick = (foodItem) => {
      console.log("food-detail", foodItem);
      navigate(`/restaurant/${restaurantId}/food-detail/${foodItem.id}`, { state: foodItem });
    };

  return (
    <div className={styles.menuContainer}>
        {menuItems.map((item) => (
      
              <div className={styles.foodInfo}>
                <FoodItem 
                    nameRestaurant={nameRes}
                    image={item.image} 
                    name={item.name}
                    price={item.price}
                    onClick={() => handleClick(item)}
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
