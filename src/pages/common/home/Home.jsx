import React from 'react'
import ItemRestaurant from '../card/ItemRestaurant'
import backgroundLogin from '../../../assets/Img/anh2.jpg';
import Header from '../../../components/header/Header';
import Banner from '../../../components/home-baner/Banner';
import Explore from '../../../ui/explore/Explore';
import banner1 from '../../../assets/Img/loginpage1.jpg';
import styles from './Home.module.css';
export default function Home() {
  return (
   
      <div>
            {/* Header */}
            <Header/>
            <Banner />
            <div className={styles.homeBody}>
                <Explore width="200px" imageUrl={banner1} />
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
