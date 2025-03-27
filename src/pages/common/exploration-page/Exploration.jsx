import React from 'react';
import WeatherModal from '../card/WeatherModal';
import Header from '../../../components/header/Header';
import styles from './Exploration.module.css';
import LocationCard from '../card/LocationCard';
import MealForm from '../card/MealForm';

export default function Exploration() {
  const weatherData = {
    location: "Hanoi",
    temperature: "30",
    conditionText: "Clear",
    humidity: "50",
    uvIndex: "5",
    hourlyForecasts: [
      { time: 1634620800, temperature: 30, icon: "☀️" },
      { time: 1634624400, temperature: 30, icon: "☀️" },
      { time: 1634628000, temperature: 30, icon: "☀️" },
      { time: 1634631600, temperature: 30, icon: "☀️" },
      { time: 1634635200, temperature: 30, icon: "☀️" },
      { time: 1634638800, temperature: 30, icon: "☀️" }
    ]
  };
  
  return (
    <div className={styles.container}>
      <div className={styles.header}> 
            <Header /> 
      </div> 
       <div className={styles.body}>
            <div className={styles.bodyLeft}>
                <LocationCard/>
            </div>
            <div className={styles.bodyInto}>
                <WeatherModal weatherData={weatherData} />
            </div>
            <div className={styles.bodyRight}>
                <MealForm/>
            </div>
       </div>
    </div>
  );
}
