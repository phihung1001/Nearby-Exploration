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
          console.log("Vị trí của bạn:", latitude, longitude);
          setUserLocation({ latitude, longitude });
          // Gọi API reverse geocoding của Nominatim (OpenStreetMap)
          fetch(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latitude}&lon=${longitude}`)
            .then((res) => res.json())
            .then((data) => {
            const district = data.address?.district || data.address?.suburb || data.address?.city_district || data.address?.county || "";
            const city = data.address?.city || data.address?.town || data.address?.village || "";
            const country = data.address?.country || "";
            const fullAddress = [district, city, country].filter(Boolean).join(", ") || "Không xác định";

              setDistrict(fullAddress);
              console.log("Địa chỉ :", fullAddress);
              console.log("location",data.address)
            })
            .catch((error) => {
              console.error("Lỗi reverse geocoding:", error);
              setErrorMsg("Lỗi khi lấy địa chỉ.");
            });
        },
        (error) => {
          setErrorMsg("Không lấy được vị trí của bạn.");
          console.error("Geolocation error:", error);
        }
      );
    } else {
      setErrorMsg("Trình duyệt của bạn không hỗ trợ Geolocation.");
    }
  }, []);

  return (
    <footer className={styles.footer}>
        {userLocation ? (
          <p>
             {district}
          </p>
        ) : errorMsg ? (
          <p>{errorMsg}</p>
        ) : (
          <p>Đang lấy vị trí của bạn...</p>
        )}
    </footer>
  );
}
