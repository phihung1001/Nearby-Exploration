import React from 'react'
import ItemRestaurant from '../card/ItemRestaurant'
import backgroundLogin from '../../../assets/Img/anh2.jpg';
import Header from '../../../components/header/Header';
import Banner from '../../../components/home-banner/Banner';
export default function Home() {
  return (
   
      <div>
            {/* Header */}
            <Header/>
            <Banner />
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
    );
}
