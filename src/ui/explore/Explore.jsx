import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from 'antd';
import styles from './Explore.module.css';

export default function ExploreButton({ imageUrl, width = '100%', height = '256px' }) {
  const navigate = useNavigate();

  const handleExplore = () => {
    navigate('/public/exploration'); // Chuyển đến trang danh sách món ăn
  };

  return (
    <div className={styles.container} style={{ width }}>
      <div className={styles.imageWrapper} style={{ height }}>
        <img 
          src={imageUrl} 
          alt="Hình ảnh món ăn" 
          className={styles.image}
        />
      </div>
      <Button 
        type="primary" 
        size="large" 
        className={styles.button}
        onClick={handleExplore}
      >
        Khám phá tự động
      </Button>
    </div>
  );
}
