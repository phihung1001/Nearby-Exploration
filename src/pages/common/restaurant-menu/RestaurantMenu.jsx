import React, { useEffect, useState } from "react";
import { Button } from "antd";
import { useParams ,useNavigate} from "react-router-dom";
import styles from "./RestaurantMenu.module.css";
import FoodItem from "../food-item/FoodItem";
import { getDishesByRestaurantId } from "../../../services/restaurantService";

const RestaurantMenu = ({id}) => {
  const [menuItems, setMenuItems] = useState([]);
  const [nameRes, setNameRes] = useState(null);
  const navigate = useNavigate(); 
  const { id: restaurantId } = useParams();
  const [loading, setLoading] = useState(true);
  const [visibleCount, setVisibleCount] = useState(3); // số món hiển thị ban đầu

  
  useEffect(() => {
    const fetchMenu = async () => {
      try {
        setLoading(true);
        const data = await getDishesByRestaurantId(restaurantId);
        setMenuItems(data); 
      } catch (error) {
        console.error("Lỗi khi tải menu:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMenu();
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
    {menuItems.length === 0 ? (
      <p>Nhà hàng chưa thêm món ăn nào.</p>
    ) : (
      <>
        {menuItems.slice(0, visibleCount).map((item) => (
          <div key={item.id} className={styles.foodInfo}>
            <FoodItem
              nameRestaurant={item.description}
              image={item.image}
              name={item.name}
              price={item.price}
              onClick={() => handleClick(item)}
            />
          </div>
        ))}

        {visibleCount < menuItems.length && (
          <div className={styles.orderButtonContainer}>
            <Button
              type="primary"
              size="large"
              className={styles.orderButton}
              onClick={() => setVisibleCount((prev) => prev + 6)}
            >
              Xem thêm
            </Button>
          </div>
        )}
      </>
    )}
  </div>
  );
};

export default React.memo(RestaurantMenu);
