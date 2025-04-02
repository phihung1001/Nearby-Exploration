import React, { useState } from 'react';
import { Card, Input, Button, Space,message,notification} from 'antd';
import styles from './LocationCard.module.css'; // Import file CSS Modules

export default function LocationCard({onLocationSelected}) {
  const [searchValue, setSearchValue] = useState('');
  const [location, setLocation] = useState(null);
  const [loading, setLoading] = useState(false);
  
  const fetchWeatherData = async (lat, lng) => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/weather/get-weather?latitude=${lat}&longitude=${lng}`);
      if (!response.ok) {
        throw new Error("Lỗi khi lấy dữ liệu thời tiết!");
      }
      const weatherData = await response.json();

      setLocation({ lat, lng }); // Cập nhật vị trí trên UI
      
      onLocationSelected(weatherData); // Gửi dữ liệu lên Exploration.js
      notification.success({
        message: "Thành công",
        description: "Lấy dữ liệu thời tiết thành công!",
      });

    } catch (error) {
      message.error("Không thể lấy dữ liệu thời tiết. Vui lòng thử lại!");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchValue) {
      message.error('Vui lòng nhập địa điểm!');
      return;
    }
    setLoading(true);
    try {
  
      // Gọi OpenStreetMap Nominatim API để chuyển đổi địa điểm thành tọa độ
      const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${searchValue}`);
      const data = await response.json();

      if (data && data.length > 0) {
        const { lat, lon } = data[0];
        setLocation({ lat, lng: lon });
        fetchWeatherData(lat, lon); // Gọi API thời tiết với tọa độ đã tìm được
      } else {
        message.error('Không tìm thấy địa điểm. Vui lòng kiểm tra lại!');
      }
    } catch (error) {
      message.error('Lỗi khi tìm kiếm địa điểm!');
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
    navigator.geolocation.getCurrentPosition(
      (position) => {
        fetchWeatherData(position.coords.latitude, position.coords.longitude);
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
          <p>Tọa độ: {location.lat}, {location.lng}</p>
        </div>
      )}
    </Card>
  );
}
