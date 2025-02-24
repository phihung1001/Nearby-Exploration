import React from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import styles from "./Banner.module.css";

import banner1 from "../../assets/Img/loginpage.jpg";
import banner2 from "../../assets/Img/bg.jpg";
import banner3 from "../../assets/Img/bunbohue.jpg";
import banner4 from "../../assets/Img/banhtrangtron.jpg";

export default function Banner({ 
  width = "100%", 
  height = "440px",
  images = [banner1, banner2, banner3, banner4],
}) {
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

  return (
    <div className={styles.bannerContainer} style={{ width }}>
      <Slider {...settings}>
        {images.map((img, index) => (
          <div key={index}>
            <div className={styles.imageWrapper} style={{ height }}>
              <img src={img} alt={`banner-${index}`} className={styles.bannerImage} />
            </div>
          </div>
        ))}
      </Slider>
    </div>
  );
}
