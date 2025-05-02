const BASE_URL = 'http://localhost:8080/api/weather';

export const getWeatherByCoordinates = async (lat, lng) => {
  const response = await fetch(`${BASE_URL}/get-weather?latitude=${lat}&longitude=${lng}`);

  if (!response.ok) {
    throw new Error('Không thể lấy dữ liệu thời tiết!');
  }

  return await response.json();
};