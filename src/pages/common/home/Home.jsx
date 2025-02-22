import React, { useState, useEffect } from 'react';
import ItemRestaurant from '../card/ItemRestaurant';
import backgroundLogin from '../../../assets/Img/anh2.jpg';
import Header from '../../../components/header/Header';
import Banner from '../../../components/home-baner/Banner';
import Explore from '../../../ui/explore/Explore';
import khamphaimg from '../../../assets/Img/loginpage1.jpg';
import styles from './Home.module.css';
export default function Home() {
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
              const districtInfo =
                data.address?.district ||
                data.address?.suburb ||
                data.address?.city_district ||
                data.address?.county ||
                "Không xác định";
              setDistrict(districtInfo);
              console.log("Địa chỉ quận/huyện:", districtInfo);
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
    <div>
      <Header />
      <Banner />
      <div className={styles.homeBody}>
        <Explore imageUrl={khamphaimg} width='200px' height='300px'/>
        <ItemRestaurant
          name="Cửa hàng Bún Chả Hà Nội"
          address="123 Trần Hưng Đạo, Quận 1, TP.HCM"
          latestComment="Bún chả ở đây ngon, thịt nướng thơm!"
          reviewCount={120}
          imageCount={45}
          rating={4.5}
          image={backgroundLogin}
        />
      </div>
    </div>
  );
}
