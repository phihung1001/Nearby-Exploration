import React, { useState, useEffect } from 'react';
import ItemRestaurant from '../card/ItemRestaurant';
import backgroundLogin from '../../../assets/Img/anh2.jpg';
import Header from '../../../components/header/Header';
import Banner from '../../../components/home-baner/Banner';
import Explore from '../../../ui/explore/Explore';
import khamphaimg from '../../../assets/Img/loginpage1.jpg';
import styles from './Home.module.css';
import Footer from '../../../components/footer/Footer';
import HomeBodyRightContainer from '../home-body/HomeBodyRightContainer';
export default function Home() {


  return (
    <div className={styles.homeContainer}>
      <Header />
      <Banner />
      <div className={styles.homeBody}>
        <div className={styles.stickyExplore}>
          <Explore imageUrl={khamphaimg} width="200px" height="300px" />
        </div>
        <div className={styles.homeBodyRightContainer}>
          <HomeBodyRightContainer />
        </div>
      </div>
      <Footer />
    </div>
  );
}
