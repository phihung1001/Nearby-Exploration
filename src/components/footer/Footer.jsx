import React, { useState, useEffect } from "react";
import styles from "./Footer.module.css";

export default function Footer() {
  const [userLocation, setUserLocation] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);
  const [district, setDistrict] = useState(null);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setUserLocation({ latitude, longitude });

          const apiUrl = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latitude}&lon=${longitude}`;
          fetch(apiUrl)
            .then((res) => res.json())
            .then((data) => {
              const dis_trict =
                data.address?.district ||
                data.address?.suburb ||
                data.address?.city_district ||
                data.address?.county ||
                "";
              const city =
                data.address?.city ||
                data.address?.town ||
                data.address?.village ||
                "";
              const country = data.address?.country || "";
              const fullAddress = [dis_trict, city, country]
                .filter(Boolean)
                .join(", ");
              setDistrict(fullAddress);
            })
            .catch(() => setErrorMsg("Lỗi khi lấy địa chỉ."));
        },
        () => setErrorMsg("Không lấy được vị trí của bạn.")
      );
    } else {
      setErrorMsg("Trình duyệt không hỗ trợ định vị.");
    }
  }, []);

  return (
    <footer className={styles.footer}>
      <div className={styles.footerContent}>
        <div className={styles.column}>
          <h3>Nearby Exploration</h3>
          <p>Khám phá nhà hàng, quán ăn, địa điểm quanh bạn một cách thông minh.</p>
        </div>
        <div className={styles.column}>
          <h4>Liên kết</h4>
          <ul>
            <li><a href="/">Trang chủ</a></li>
            <li><a href="/restaurants">Nhà hàng</a></li>
            <li><a href="/about">Về chúng tôi</a></li>
            <li><a href="/contact">Liên hệ</a></li>
          </ul>
        </div>
        <div className={styles.column}>
          <h4>Vị trí của bạn</h4>
          <p>
            {userLocation
              ? district || "Đang xác định..."
              : errorMsg || "Đang lấy vị trí..."}
          </p>
        </div>
      </div>
      <div className={styles.bottomBar}>
        <p>&copy; {new Date().getFullYear()} Nearby Exploration. All rights reserved.</p>
      </div>
    </footer>
  );
}
