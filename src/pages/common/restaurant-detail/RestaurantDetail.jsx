import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Header from "../../../components/header/Header";
import Banner from "../../../components/home-baner/Banner";
import InforRestaurant from "../card/inforRestaurant";
import styles from "./RestaurantDetail.module.css";
import Footer from "../../../components/footer/Footer";
import Explore from '../../../ui/explore/Explore';
import khamphaimg from '../../../assets/Img/loginpage1.jpg';
import ReviewSummary from "../../../ui/review-sumary/ReviewSummary";
import HomeBodyRightContainer from '../home-body/HomeBodyRightContainer';

export default function RestaurantDetail() {
  const { id } = useParams(); // Lấy ID từ URL
  const [restaurant, setRestaurant] = useState(null);
  const [loading, setLoading] = useState(true);
  const myRatings = {
    viTri: 7.8,
    giaCa: 8.2,
    chatLuong: 8.2,
    phucVu: 7.6,
    khongGian: 7.4,
    binhLuan: 15,
  };
  useEffect(() => {
    // Fetch dữ liệu từ API
    fetch("/restaurants.json")
      .then((res) => res.json())
      .then((data) => {
        const foundRestaurant = data.find((r) => r.id.toString() === id);
        if (foundRestaurant) {
          setRestaurant(foundRestaurant);
        } else {
          console.error("Không tìm thấy nhà hàng");
        }
      })
      .catch((error) => console.error("Lỗi tải dữ liệu:", error))
      .finally(() => setLoading(false)); // Dừng trạng thái loading
 
  }, [id]);
  console.log("foundRestaurant",restaurant);
  if (loading) return <p>Đang tải...</p>;
  if (!restaurant) return <p>Không tìm thấy nhà hàng.</p>;

  return (
    <div  style={{display:"flex", flexDirection:"column"}}>
      <Header />
      <div className={styles.containerDetail}>

        <div className={styles.bannerDetail}>
          <div className={styles.left}>
            <Banner images={restaurant.image} width="100%" height="300px" />
          </div>
          <div className={styles.right}>
            <InforRestaurant
                breadcrumb={restaurant.city + " > " + restaurant.suburb + " > " + restaurant.district}
                name={restaurant.name}
                shortDesc="Quán ăn - Món Việt - Gia đình, Nhóm hội, Giao hàng..."
                branchLink="#"
                ratings={myRatings}
                address={restaurant.address}
                time="Chưa mở cửa (10:00 - 22:00)"
                price="50.000đ - 100.000đ"
            />
          </div>
        </div>

        <div className={styles.bodyDetail}>
            <div className={styles.stickyExplore}>
              <Explore imageUrl={khamphaimg} width="200px" height="300px" />
            </div>
            <div>
                <HomeBodyRightContainer />
            </div>
            <div className={styles.reviewSumary}>
              <ReviewSummary />
            </div>
        </div>
        
      </div>
      <Footer/>
    </div>
    
  );
}
