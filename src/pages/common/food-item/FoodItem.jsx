import React from "react";
import styles from "./FoodItem.module.css";

const FoodItem = ({ image, name, price ,nameRestaurant}) => {
  return (
    <div className={styles.foodItem}>
      <img src={image} alt={name} className={styles.foodImage} />
      <div className={styles.foodInfo}>
        <h3 className={styles.foodName}>{name}</h3>
        <p className={styles.foodDescription}>{nameRestaurant}</p>
      </div>
      <span className={styles.foodPrice}>{price}đ</span>
    </div>
  );
};

export default FoodItem;
