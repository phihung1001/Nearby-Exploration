import React, { useState } from "react";
import styles from "./Header.module.css";
import {message, notification } from "antd";
import SearchBar from "../../ui/search-bar/SearchBar";
import FilterDropdown from "../../ui/filter-dropdown/FilterDropdown";
import LanguageSwitcher from "../../ui/language-button/LanguageSwitcher";
import AuthButton from "../../ui/authlogin-button/AuthButton";
import { Link,useNavigate } from "react-router-dom";

export default function Header() {
  const [filterData, setFilterData] = useState(null);
  const [searchText, setSearchText] = useState("");
  const navigate = useNavigate();

  // Hàm xử lý thay đổi bộ lọc
  const handleFilterChange = (data) => {
    setFilterData(data); // Cập nhật dữ liệu bộ lọc
  };

  // Hàm xử lý tìm kiếm
  const handleSearch = async () => {
    if (!searchText && !filterData) {
      // Nếu không có từ khóa tìm kiếm và không có bộ lọc
      notification.error({
        message: "Tìm kiếm thất bại",
        description: "Bạn cần nhập từ khóa tìm kiếm hoặc chọn bộ lọc để tìm kiếm nhà hàng.",
    });
      return;
    }
    const searchParams = new URLSearchParams();
    if (searchText) {
      searchParams.append('name', searchText);
    }
    if (filterData?.provinceId) {
      searchParams.append('cityId', filterData.provinceId);
    }
    if (filterData?.districtId) {
      searchParams.append('districtId', filterData.districtId);
    }
      navigate(`/public/restaurant-list?${searchParams.toString()}`);
  };

  return (
    <div className={styles.headerContainer}>
      <div className={styles.headerRight}>
        <Link to="/" className={styles.logo}>
          Nearby Exploration
        </Link>      
      </div>
      <div className={styles.headerRight}>
        <SearchBar searchText={searchText} setSearchText={setSearchText} onSearch={handleSearch} />
        <FilterDropdown onFilterChange={handleFilterChange} />
      </div>
      <div className={styles.headerRight}>
        <AuthButton />
        <LanguageSwitcher />
      </div>
    </div>
  );
}
