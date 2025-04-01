import React from 'react';
import styles from './DishModal.module.css';

export default function DishModal({ isOpen, onClose, data }) {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
        <h2>{data.title}</h2>
        <ul>
          {data.dishesResponseList.map((dish, index) => (
            <li key={index}>
              <h3>{dish.name}</h3>
              <p>{dish.description}</p>
            </li>
          ))}
        </ul>
        <button className={styles.closeButton} onClick={onClose}>Close</button>
      </div>
    </div>
  );
};