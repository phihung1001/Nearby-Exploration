import React, { useState } from 'react';
import styles from './DishModal.module.css';

export default function DishModal({ isOpen, onClose, data }) {
  const [activeIndex, setActiveIndex] = useState(null);

  if (!isOpen) return null;

  const handleToggle = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  return (
    <div className={styles.modalContentDishModal}>
      <div className={styles.modalOverlayDishModal}>
        <h2>{data.title}</h2>
        <p className={styles.description}>{data.description}</p>

        <div className={styles.dishList}>
          {data.dishes.map((dish, index) => (
            <button
              key={index}
              className={`${styles.dishButton} ${activeIndex === index ? styles.active : ''}`}
              onClick={() => handleToggle(index)}
            >
              {dish.name}
            </button>
          ))}
        </div>

        {activeIndex !== null && (
          <div className={styles.dishDetails}>
            <h3>{data.dishes[activeIndex].name}</h3>
            <p>{data.dishes[activeIndex].description}</p>
          </div>
        )}
      </div>
      
    </div>
  );
}
