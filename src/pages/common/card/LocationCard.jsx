import React, { useState } from 'react';
import { Card, Input, Button, Space } from 'antd';
import styles from './LocationCard.module.css'; // Import file CSS Modules

export default function LocationCard() {
  const [searchValue, setSearchValue] = useState('');
  const [location, setLocation] = useState(null);

  const handleSearch = () => {
    // TODO: Xử lý logic tìm kiếm vị trí thủ công
    console.log('Tìm kiếm vị trí:', searchValue);
  };

  const handleUseCurrentLocation = () => {
    if (!navigator.geolocation) {
      alert('Trình duyệt không hỗ trợ Geolocation.');
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setLocation({
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        });
      },
      (error) => {
        alert('Không thể lấy vị trí hiện tại. Vui lòng bật GPS hoặc thử lại.');
        console.error(error);
      }
    );
  };

  return (
    <Card
      title="Cài Đặt Vị Trí"
      className={styles.locationCard}
    >
      <p className={styles.locationText}>
        Chúng tôi tự động xác định vị trí của bạn hoặc bạn có thể nhập thủ công
      </p>

      <Space.Compact className={styles.searchContainer}>
        <Input
          placeholder="Tìm kiếm vị trí"
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
        />
        <Button type="primary" onClick={handleSearch}>
          Tìm kiếm
        </Button>
      </Space.Compact>

      <Button
        block
        className={styles.currentLocationBtn}
        onClick={handleUseCurrentLocation}
      >
        Sử dụng vị trí hiện tại của tôi
      </Button>

      {location && (
        <div className={styles.locationResult}>
          <strong>Vị trí hiện tại:</strong>
          <p>Lat: {location.lat}</p>
          <p>Lng: {location.lng}</p>
        </div>
      )}
    </Card>
  );
}
