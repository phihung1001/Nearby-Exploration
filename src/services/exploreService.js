export const getDishesByWeather = async (weatherData, mealSettings) => {
    const body = {
      excludedFoods: mealSettings.excludedDishes,
      mealType: mealSettings.mealType,
      numberOfPeople: mealSettings.numberOfPeople.toString(),
      specialRequests: mealSettings.specialRequest,
      location: weatherData.location,
      weather: [weatherData.conditionText, `${weatherData.temperature} độ`],
    };
  
    const response = await fetch('http://localhost:8080/api/openai/explore', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
  
    if (!response.ok) {
      throw new Error('Không thể lấy dữ liệu từ API explore');
    }
  
    return await response.json();
  };