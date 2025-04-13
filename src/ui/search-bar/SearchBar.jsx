import React, { useState } from "react";
import { Input, Button, notification } from "antd";
import { SearchOutlined, CameraOutlined } from "@ant-design/icons";
import CameraModal from "../CameraModal";  

export default function SearchBar({ searchText, setSearchText, onSearch }) {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleSearch = () => {
     onSearch();  // Gọi hàm onSearch khi nhấn tìm kiếm
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
        onChange={(e) => setSearchText(e.target.value)}  // Cập nhật searchText
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

      <CameraModal open={isModalOpen} onCancel={handleModalCancel} />
    </div>
  );
}
