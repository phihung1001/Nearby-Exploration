import React from "react";
import styles from "./inforRestaurant.module.css";
import { EnvironmentOutlined, ClockCircleOutlined, DollarCircleOutlined } from "@ant-design/icons";

export default function inforRestaurant({
  breadcrumb = "Hà Nội > Quận Hai Bà Trưng > Khu vực Hồ Thiền Quang",
  name = "Phở Cuốn Hương Mai - Lê Đại Hành",
  shortDesc = "Quán ăn - Món Việt - Gia đình, Nhóm hội, Giao hàng...",
  branchLink = "#",
  ratings = {
    viTri: 7.8,
    giaCa: 8.2,
    chatLuong: 8.2,
    phucVu: 7.6,
    khongGian: 7.9,
    binhLuan: 15,
  },
  address = "19 Lê Đại Hành, P. Lê Đại Hành, Quận Hai Bà Trưng, Hà Nội",
  time = "Chưa mở cửa (10:00 - 22:00)",
  price = "50.000đ - 100.000đ",
}) {
  return (
    <div className={styles.detailContainer}>
      {/* Breadcrumb */}
      <div className={styles.breadcrumb}>
        <span>{breadcrumb}</span>
      </div>

      {/* Tên nhà hàng */}
      <h1 className={styles.title}>{name}</h1>

      {/* Mô tả ngắn */}
      <div className={styles.subtitle}>
        {shortDesc}
        <a href={branchLink} className={styles.moreInfo}>
          Chi nhánh
        </a>
      </div>

      {/* Khu vực đánh giá */}
      <div className={styles.ratings}>
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.viTri}</span>
          <span className={styles.ratingLabel}>Vị trí</span>
        </div>
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.giaCa}</span>
          <span className={styles.ratingLabel}>Giá cả</span>
        </div>
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.chatLuong}</span>
          <span className={styles.ratingLabel}>Chất lượng</span>
        </div>
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.phucVu}</span>
          <span className={styles.ratingLabel}>Phục vụ</span>
        </div>
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.khongGian}</span>
          <span className={styles.ratingLabel}>Không gian</span>
        </div>

        {/* Dấu phân cách */}
        <div className={styles.verticalDivider}></div>

        {/* Số bình luận */}
        <div className={styles.ratingItem}>
          <span className={styles.ratingNumber}>{ratings.binhLuan}</span>
          <span className={styles.ratingLabel}>Bình luận</span>
        </div>
      </div>

      {/* Thông tin chi tiết */}
      <div className={styles.info}>
        <p>
          <EnvironmentOutlined className={styles.icon} />
          {address}
        </p>
        <p>
          <ClockCircleOutlined className={styles.icon} />
          {time}
        </p>
        <p>
          <DollarCircleOutlined className={styles.icon} />
          {price}
        </p>
      </div>
    </div>
  );
}
