import React from "react";
import { Progress, Button } from "antd";
import styles from "./ReviewSummary.module.css";

const ReviewSummary = () => {
  const ratings = {
    total: 38,
    excellent: 17,
    good: 17,
    average: 3,
    bad: 1,
    criteria: [
      { label: "Vị trí", value: 8.2 },
      { label: "Giá cả", value: 8.0 },
      { label: "Chất lượng", value: 9.2 },
      { label: "Phục vụ", value: 8.8 },
      { label: "Không gian", value: 8.3 },
    ],
    overall: 8.5,
  };

  return (
    <div className={styles.reviewContainer}>
      <div className={styles.ratingHeader}>
        <strong>{ratings.total}</strong> bình luận đã chia sẻ
      </div>
      <div className={`${styles.ratingItem} ${styles.tuyetVoi}`}>
        {ratings.excellent} Tuyệt vời
      </div>
      <div className={`${styles.ratingItem} ${styles.khaTot}`}>
        {ratings.good} Khá tốt
      </div>
      <div className={`${styles.ratingItem} ${styles.trungBinh}`}>
        {ratings.average} Trung bình
      </div>
      <div className={`${styles.ratingItem} ${styles.kem}`}>
        {ratings.bad} Kém
      </div>

      <div className={styles.progressContainer}>
        {ratings.criteria.map((item, index) => (
          <div key={index}>
            <div className={styles.progressLabel}>
              <span>{item.label}</span>
              <span>{item.value}</span>
            </div>
            <Progress percent={item.value * 10} showInfo={false} strokeColor="#e74c3c" />
          </div>
        ))}
      </div>

      <div className={styles.ratingScore}>{ratings.overall} điểm - Khá tốt</div>

      <Button className={styles.writeCommentBtn} icon="edit">
        Viết bình luận
      </Button>
    </div>
  );
};

export default ReviewSummary;
