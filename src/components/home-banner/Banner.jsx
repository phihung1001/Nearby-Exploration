import React from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import styles from "./Banner.module.css";

import banner1 from '../../assets/Img/image1.png';
import banner2 from "../../assets/Img/anh2.jpg";
import banner3 from "../../assets/Img/bground3.jpg";
import banner4 from "../../assets/Img/bgleft.jpg";

export default function Banner() {
  const settings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    arrows: false,
  };

  const images = [banner1, banner2, banner3, banner4];

  return (
    <div className={styles.bannerContainer}>
      <Slider {...settings}>
        {images.map((img, index) => (
          <div key={index}>
            <div className={styles.imageWrapper}>
              <img src={img} alt={`banner-${index}`} className={styles.bannerImage} />
            </div>
          </div>
        ))}
      </Slider>
    </div>
  );
}
