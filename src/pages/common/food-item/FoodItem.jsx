import React from "react";
import styles from "./FoodItem.module.css";
import imgFood from "../../../assets/Img/loginpage.jpg";
const FoodItem = ({ image, name, price ,nameRestaurant, onClick}) => {
  return (
    <div className={styles.foodItem} onClick={onClick}>
      <img
        className={styles.foodImage}
        src={imgFood}
        alt={name}
  
      />
      <div className={styles.foodInfo}>
        <h3 className={styles.foodName}>{name}</h3>
        <p className={styles.foodDescription}>{nameRestaurant}</p>
      </div>
      <span className={styles.foodPrice}>{price}đ</span>
    </div>
  );
};

export default FoodItem;
