import React, { useState } from 'react';
import { Card, Input, Button, Space, message, notification } from 'antd';
import styles from './LocationCard.module.css';
import { getCoordinatesFromLocation } from '../../../services/locationService';
import { getWeatherByCoordinates } from '../../../services/weatherService';

export default function LocationCard({ onLocationSelected }) {
  const [searchValue, setSearchValue] = useState('');
  const [location, setLocation] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!searchValue) {
      message.error('Vui lòng nhập địa điểm!');
      return;
    }
    setLoading(true);
    try {
      const { lat, lng } = await getCoordinatesFromLocation(searchValue);
      setLocation({ lat, lng });

      const weatherData = await getWeatherByCoordinates(lat, lng);
      onLocationSelected(weatherData);

      notification.success({
        message: 'Thành công',
        description: 'Lấy dữ liệu thời tiết thành công!',
      });
    } catch (error) {
      message.error(error.message || 'Đã xảy ra lỗi!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleUseCurrentLocation = () => {
    if (!navigator.geolocation) {
      alert('Trình duyệt không hỗ trợ Geolocation.');
      return;
    }
    setLoading(true);
    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        setLocation({ lat, lng });

        try {
          const weatherData = await getWeatherByCoordinates(lat, lng);
          onLocationSelected(weatherData);
          notification.success({
            message: 'Thành công',
            description: 'Lấy dữ liệu thời tiết thành công!',
          });
        } catch (error) {
          message.error('Không thể lấy dữ liệu thời tiết!');
          console.error(error);
        } finally {
          setLoading(false);
        }
      },
      (error) => {
        alert('Không thể lấy vị trí hiện tại. Vui lòng bật GPS hoặc thử lại.');
        console.error(error);
        setLoading(false);
      }
    );
  };

  return (
    <Card title="Cài Đặt Vị Trí" className={styles.locationCard}>
      <p className={styles.locationText}>
        Chúng tôi tự động xác định vị trí của bạn hoặc bạn có thể nhập thủ công
      </p>

      <Space.Compact className={styles.searchContainer}>
        <Input
          placeholder="Tìm kiếm vị trí"
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
        />
        <Button type="primary" onClick={handleSearch} loading={loading}>
          Tìm kiếm
        </Button>
      </Space.Compact>

      <Button
        block
        className={styles.currentLocationBtn}
        onClick={handleUseCurrentLocation}
        loading={loading}
      >
        Sử dụng vị trí hiện tại của tôi
      </Button>

      {location && (
        <div className={styles.locationResult}>
          <strong>Vị trí hiện tại:</strong>
          <p>Tọa độ: {location.lat}, {location.lng}</p>
        </div>
      )}
    </Card>
  );
}
