import React, { useState } from "react";
import { Input, Button, message } from "antd";
import { SearchOutlined, CameraOutlined } from "@ant-design/icons";
import CameraModal from "../CameraModal";  

export default function SearchBar({ onSearch }) {
  const [searchText, setSearchText] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleSearch = () => {
    if (!searchText) {
      message.error("Vui lòng nhập từ khóa");
      return;
    }
    console.log("Tìm kiếm với:", searchText);
    if (onSearch) {
      onSearch({ searchText });
    }
  };

  const handleCameraClick = () => {
    setIsModalOpen(true);
  };

  const handleModalCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <div>
      <Input
        placeholder="Tìm kiếm món ăn..."
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        onPressEnter={handleSearch}
        prefix={<SearchOutlined style={{ color: "#999" }} />}
        suffix={
          <CameraOutlined
            style={{ color: "#ffa006", cursor: "pointer" }}
            onClick={handleCameraClick}
          />
        }
        style={{ width: "300px" }}
      />
      <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
        Tìm kiếm
      </Button>

      {/* Cập nhật prop từ 'visible' thành 'open' */}
      <CameraModal open={isModalOpen} onCancel={handleModalCancel} />
    </div>
  );
}
