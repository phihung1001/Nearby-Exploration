import React from 'react';
import { Card, Row, Col } from 'antd';
import styles from './WeatherForm.module.css';

export default function WeatherForm({ weatherData }) {
  return (
    <div className={styles.containerWeatherForm}>
      <Card title="Thông Tin Thời Tiết" className={styles.cardWeatherForm}>
        <div className={styles.headerWeatherForm}>
          <h2> {weatherData.location}</h2>
        </div>
        <div className={styles.mainWeatherForm}>
          <div className={styles.temperatureWeatherForm}>
            {weatherData.temperature}°C
          </div>
          <div className={styles.conditionWeatherForm}>
            {weatherData.conditionText}
          </div>
        </div>
        <div className={styles.detailsWeatherForm}>
          <div className={styles.detailItemWeatherForm}>
            <span className={styles.detailLabel}>Độ ẩm:</span>
            <span className={styles.detailValue}>{weatherData.humidity}%</span>
          </div>
          <div className={styles.detailItemWeatherForm}>
            <span className={styles.detailLabel}>Chỉ số UV:</span>
            <span className={styles.detailValue}>{weatherData.uvIndex}</span>
          </div>
        </div>
        <div className={styles.forecastSectionWeatherForm}>
          <h3>Dự Báo Theo Giờ</h3>
          <Row gutter={[16, 16]} className={styles.forecast}>
            {weatherData.forecast.slice(0, 6).map((forecast, index) => (
              <Col key={index} span={4} className={styles.forecastItem}>
                <p className={styles.forecastTimeWeatherForm}>
                {new Date(forecast.time).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
                <p className={styles.forecastTempWeatherForm}>{forecast.temperature}°C</p>
                <p className={styles.forecastIconWeatherForm}>{forecast.icon}</p>
              </Col>
            ))}
          </Row>
        </div>
      </Card>
    </div>
  );
}
