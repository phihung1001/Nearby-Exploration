import React from 'react';
import { Card, Row, Col } from 'antd';
import styles from './WeatherForm.module.css';

export default function WeatherForm({ weatherData }) {
  return (
    <div className={styles.container}>
      <Card title="Thông Tin Thời Tiết" className={styles.card}>
        <div className={styles.header}>
          <h2>{weatherData.location}</h2>
        </div>
        <div className={styles.main}>
          <div className={styles.temperature}>
            {weatherData.temperature}°C
          </div>
          <div className={styles.condition}>
            {weatherData.conditionText}
          </div>
        </div>
        <div className={styles.details}>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Độ ẩm:</span>
            <span className={styles.detailValue}>{weatherData.humidity}%</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Chỉ số UV:</span>
            <span className={styles.detailValue}>{weatherData.uvIndex}</span>
          </div>
        </div>
        <div className={styles.forecastSection}>
          <h3>Dự Báo Theo Giờ</h3>
          <Row gutter={[16, 16]} className={styles.forecast}>
            {weatherData.hourlyForecasts.slice(0, 6).map((forecast, index) => (
              <Col key={index} span={4} className={styles.forecastItem}>
                <p className={styles.forecastTime}>
                  {new Date(forecast.time * 1000).getHours()}:00
                </p>
                <p className={styles.forecastTemp}>{forecast.temperature}°C</p>
                <p className={styles.forecastIcon}>{forecast.icon}</p>
              </Col>
            ))}
          </Row>
        </div>
      </Card>
    </div>
  );
}
