import React, { useState, useEffect } from 'react';
import WeatherModal from '../card/WeatherModal';
import Header from '../../../components/header/Header';
import styles from './Exploration.module.css';
import LocationCard from '../card/LocationCard';
import MealForm from '../card/MealForm';
import DishModal from '../card/DishModal';
import { getDishesByWeather } from '../../../services/exploreService';

export default function Exploration() {
  const [weatherData, setWeatherData] = useState(null);
  const [isWeatherModalVisible, setIsWeatherModalVisible] = useState(false);
  const [dishesData, setDishesData] = useState(null);
  const [isDishModalVisible, setIsDishModalVisible] = useState(false);

  const [mealSettings, setMealSettings] = useState({
    excludedDishes: [],
    mealType: 'single',
    numberOfPeople: 1,
    specialRequest: '',
  });

  const handleLocationSelected = (data) => {
    setWeatherData(data);
    setIsWeatherModalVisible(true);
    console.log('Dữ liệu thời tiết:', data);
  };

  const handleMealSettingsChange = (newSettings) => {
    setMealSettings(newSettings);
  };

  useEffect(() => {
    const fetchDishes = async () => {
      try {
        const data = await getDishesByWeather(weatherData, mealSettings);
        setDishesData(data);
        setIsDishModalVisible(true);
        console.log('Dữ liệu từ API explore:', data);
      } catch (error) {
        console.error(error);
      }
    };

    if (weatherData && mealSettings) {
      fetchDishes();
    }
  }, [weatherData, mealSettings]);

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
            <MealForm onMealSettingsChange={handleMealSettingsChange} />
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
