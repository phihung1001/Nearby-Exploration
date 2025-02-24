import React from "react";
import styles from "./Header.module.css";
import SearchBar from "../../ui/search-bar/SearchBar";
import FilterDropdown from "../../ui/FilterDropdown";
import LanguageSwitcher from "../../ui/language-button/LanguageSwitcher";
import AuthButton from "../../ui/authlogin-button/AuthButton";


export default function Header({ onSearch }) {
  return (
    
    <div className={styles.headerContainer}>
      <div className={styles.headerRight}>
      <div className={styles.logo}>Nearby Exploration</div>
      </div>
      <div className={styles.headerRight}>
        <SearchBar onSearch={onSearch} />
        <FilterDropdown onSearch={onSearch} />
      </div>
        <div className={styles.headerRight}>
            <AuthButton />
            <LanguageSwitcher />
        </div>
    </div>
  );
}
