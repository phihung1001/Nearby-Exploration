import React from "react";
import styles from "./Header.module.css";
import SearchBar from "../../ui/search-bar/SearchBar";
import FilterDropdown from "../../ui/FilterDropdown";
import LanguageSwitcher from "../../ui/language-button/LanguageSwitcher";
import AuthButton from "../../ui/authlogin-button/AuthButton";
import { Link } from "react-router-dom";


export default function Header({ onSearch }) {
  return (
    
    <div className={styles.headerContainer}>
      <div className={styles.headerRight}>
        <Link to="/" className={styles.logo}>
          Nearby Exploration
        </Link>      
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
