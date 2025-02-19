import React from "react";
import styles from "./ItemRestaurant.module.css";

export default function ItemRestaurant({ 
    name, 
    address, 
    image, 
    latestComment, 
    reviewCount, 
    imageCount, 
    rating 
}) {
    return (
        <div className={styles.itemRestaurant}>
            <img className={styles.restaurantImage} src={image} alt={name} />
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
                        ⭐ {rating} / 5
                    </div>
                </div>
            </div>
        </div>
    );
}
