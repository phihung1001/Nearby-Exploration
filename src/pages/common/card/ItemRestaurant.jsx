import React from "react";
import styles from "./ItemRestaurant.module.css";
import defaultImage from "../../../../src/assets/Img/loginpage.jpg";

export default function ItemRestaurant({ 
    name, 
    address, 
    image, 
    latestComment, 
    reviewCount, 
    imageCount, 
    rating, 
    onClick,
    onSave
}) {
    return (
        <div className={styles.itemRestaurant} onClick={onClick}>
            <img 
                className={styles.restaurantImage} 
                src={image} 
                alt={name}  
                onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = defaultImage;
                }}
            />
            <div className={styles.content}>
                <div className={styles.header}>
                    <h3 className={styles.title}>{name}</h3> {/* Đặt class title vào đây */}
                    <p className={styles.address}>{address}</p>
                </div>
                <div className={styles.body}>
                  <p className={styles.comment}><strong>Bình luận mới nhất:</strong> {latestComment || "Chưa có bình luận nào"}</p>
                  <div className={styles.stats}>
                      <span>🍽 {reviewCount} đánh giá</span>
                      <span>📸 {imageCount} ảnh</span>
                  </div>
                  <div className={styles.ratingAndSave}>
                      <span className={styles.rating}>⭐ {parseFloat(rating).toFixed(1)} / 10</span>
                      <button 
                          className={styles.saveButton}
                          onClick={(e) => {
                              e.stopPropagation();
                              onSave && onSave();
                          }}
                      >
                          💾 Lưu
                      </button>
                  </div>
                </div>
            </div>
        </div>
    );
}
