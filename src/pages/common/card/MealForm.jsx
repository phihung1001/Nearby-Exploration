import React, { useState } from 'react';
import styles from './MealForm.module.css'; // Import file CSS Modules
import { notification } from 'antd';

export default function MealForm() {
  const [excludedDishes, setExcludedDishes] = useState([]);
  const [excludedDishInput, setExcludedDishInput] = useState('');
  const [numberOfPeople, setNumberOfPeople] = useState(1);
  const [mealType, setMealType] = useState('single'); // 'single' hoặc 'full'
  const [specialRequest, setSpecialRequest] = useState('');
  const [showAdvanced, setShowAdvanced] = useState(false);

  // Xử lý thêm món bị loại trừ
  const handleAddExcludedDish = () => {
    if (excludedDishInput.trim() !== '') {
      setExcludedDishes([...excludedDishes, excludedDishInput.trim()]);
      setExcludedDishInput('');
    }
  };

  // Xóa món khỏi danh sách loại trừ
  const handleRemoveExcludedDish = (dish) => {
    setExcludedDishes(excludedDishes.filter((d) => d !== dish));
  };

  // Lưu cài đặt (submit form)
  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = {
      excludedDishes,
      numberOfPeople,
      mealType,
      specialRequest,
    };
    console.log('Dữ liệu cài đặt:', formData);
    notification.success({
      message: 'Cài đặt đã được lưu!'
  });
  };

  return (
    <form className={styles.mealForm} onSubmit={handleSubmit}>
      <h2>Cài Đặt Món Ăn</h2>
      <p>Tùy chỉnh gợi ý món ăn của bạn</p>

      {/* LOẠI TRỪ MÓN ĂN */}
      <h3>Loại Trừ Món Ăn</h3>
      <p>Chỉ định các món bạn không thích, dị ứng hoặc cần tránh</p>
      <div className={styles.exclusionSection}>
        <input
          type="text"
          placeholder="Nhập món ăn hoặc nguyên liệu"
          value={excludedDishInput}
          onChange={(e) => setExcludedDishInput(e.target.value)}
        />
        <button type="button" onClick={handleAddExcludedDish}>
          Thêm
        </button>
      </div>
      {excludedDishes.length === 0 ? (
        <p>Chưa có món ăn nào bị loại trừ</p>
      ) : (
        <ul>
          {excludedDishes.map((dish, index) => (
            <li key={index}>
              {dish}
              <button type="button" onClick={() => handleRemoveExcludedDish(dish)}>
                X
              </button>
            </li>
          ))}
        </ul>
      )}

      {/* CÀI ĐẶT NÂNG CAO */}
      <div
        className={styles.advancedToggle}
        onClick={() => setShowAdvanced(!showAdvanced)}
      >
        <span>Cài Đặt Nâng Cao</span>
        <span>{showAdvanced ? '▲' : '▼'}</span>
      </div>

      {showAdvanced && (
        <div className={styles.advancedSection}>
          <h4>Số Người Dùng Bữa</h4>
          <p>Bao nhiêu người sẽ dùng bữa</p>
          <input
            type="number"
            min="1"
            value={numberOfPeople}
            onChange={(e) => setNumberOfPeople(e.target.value)}
          />
          <p>{numberOfPeople} người</p>

          <h4>Loại Bữa Ăn</h4>
          <p>Bạn dự định dùng bữa kiểu gì</p>
          <div>
            <label>
              <input
                type="radio"
                name="mealType"
                value="single"
                checked={mealType === 'single'}
                onChange={() => setMealType('single')}
              />
              Món đơn
            </label>
            <label>
              <input
                type="radio"
                name="mealType"
                value="full"
                checked={mealType === 'full'}
                onChange={() => setMealType('full')}
              />
              Đầy đủ
            </label>
          </div>

          <h4>Yêu Cầu Đặc Biệt</h4>
          <p>Nhập yêu cầu đặc biệt của bạn</p>
          <textarea
            value={specialRequest}
            onChange={(e) => setSpecialRequest(e.target.value)}
            placeholder="Nhập yêu cầu đặc biệt của bạn"
          />
        </div>
      )}

      <button type="submit">Lưu Cài Đặt</button>
    </form>
  );
}
