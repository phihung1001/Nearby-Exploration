import React from "react";
import styles from "./ItemRestaurant.module.css";
import defaultImage from "../../../../src/assets/Img/loginpage.jpg"

export default function ItemRestaurant({ 
    name, 
    address, 
    image, 
    latestComment, 
    reviewCount, 
    imageCount, 
    rating, 
    onClick // Thêm prop onClick
}) {
    return (
        <div className={styles.itemRestaurant} onClick={onClick}> 
            <img className={styles.restaurantImage} 
                src={image} 
                alt={name}  
                onError={(e) => {
                    e.target.onerror = null; // Ngăn lặp vô hạn khi ảnh mặc định cũng lỗi
                    e.target.src = defaultImage;
                }}
            />
            <div className={styles.content}>
                <div className={styles.header}>
                    <h3>{name}</h3>
                    <p className={styles.address}>{address}</p>
                </div>
                <div className={styles.body}>
                    <p className={styles.comment}><strong>Bình luận mới nhất:</strong> {latestComment}</p>
                    <div className={styles.stats}>
                        <span>📢 {reviewCount} đánh giá</span>
                        <span>📸 {imageCount} ảnh</span>
                    </div>
                    <div className={styles.rating}>
                        ⭐ {rating} / 10
                    </div>
                </div>
            </div>
        </div>
    );
}
