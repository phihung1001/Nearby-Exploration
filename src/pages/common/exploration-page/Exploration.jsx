import React, { useState, useEffect } from 'react';
import WeatherModal from '../card/WeatherModal';
import Header from '../../../components/header/Header';
import styles from './Exploration.module.css';
import LocationCard from '../card/LocationCard';
import MealForm from '../card/MealForm';
import DishModal from '../card/DishModal';

export default function Exploration() {
  const [weatherData, setWeatherData] = useState(null);
  const [isWeatherModalVisible, setIsWeatherModalVisible] = useState(false);
  const [dishesData, setDishesData] = useState(null);
  const [isDishModalVisible, setIsDishModalVisible] = useState(false);
  
  // Nhận dữ liệu từ LocationCard và hiển thị WeatherModal
  const handleLocationSelected = async (data) => {
    setWeatherData(data);
    setIsWeatherModalVisible(true);
    console.log('Dữ liệu thời tiết:', data); 

    const fetchExploreData = async () => {
      try {
        const body = {
          excludedFoods: [],
          mealType: "", 
          numberOfPeople: "1", 
          specialRequests: "", 
          location: data.location, 
          weather: [data.conditionText, `${data.temperature} độ`], // Tạo mảng thời tiết
        };

        const exploreResponse = await fetch('http://localhost:8080/api/openai/explore', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(body),
        });

        if (!exploreResponse.ok) {
          throw new Error('Không thể lấy dữ liệu từ API explore');
        }

        const exploreData = await exploreResponse.json();
        console.log('Dữ liệu từ API explore:', exploreData);
        setDishesData(exploreData);
        setIsDishModalVisible(true);
      } catch (error) {
        console.error(error);
      }
    };

    fetchExploreData();
  };
  
  return (
  <div className={styles.containerExploration}>
    <div className={styles.headerExploration}> 
      <Header /> 
    </div> 
    <div className={styles.titleExploration}>
      <h1 className={styles.titleh1}>Khám Phá Món Ăn</h1>
      <p className={styles.titlep}>Chúng tôi sẽ giúp bạn tìm món ăn ngon nhất quanh địa điểm của bạn</p>
    </div>
    <div className={`${styles.bodyExploration} ${weatherData ? styles.splitView : styles.centerView}`}>
      <div className={styles.bodyLeft}>
        <div className={styles.bodyLeftTop}>  
          <LocationCard onLocationSelected={handleLocationSelected} />
        </div>
       <div className={styles.bodyLeftBottom}>
          <MealForm/>     
       </div>
      </div>
      {weatherData && (
        <div className={styles.bodyRight}>
          <div className={styles.bodyRightTop}>
            <WeatherModal
              weatherData={weatherData}
              isVisible={isWeatherModalVisible}
              onClose={() => setIsWeatherModalVisible(false)}
            />
          </div>
          <div className={styles.bodyRightBottom}>
            {dishesData && (
              <DishModal
                isOpen={isDishModalVisible}
                onClose={() => setIsDishModalVisible(false)}
                data={dishesData}
              />    
            )}
          </div>
        </div>
      )}
    </div>
  </div>
  );
}
