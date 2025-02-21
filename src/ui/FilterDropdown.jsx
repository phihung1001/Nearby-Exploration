import React, { useState } from "react";
import { Select, Dropdown, Button, Space } from "antd";
import { FilterOutlined } from "@ant-design/icons";

export default function FilterDropdown({ onSearch }) {
  const [selectedProvince, setSelectedProvince] = useState(null);
  const [selectedDistrict, setSelectedDistrict] = useState(null);
  const [filterVisible, setFilterVisible] = useState(false);

  const provinces = [
    { value: "Hà Nội", label: "Hà Nội" },
    { value: "Hồ Chí Minh", label: "Hồ Chí Minh" },
  ];
  const districts = {
    "Hà Nội": [{ value: "Ba Đình", label: "Ba Đình" }, { value: "Hoàn Kiếm", label: "Hoàn Kiếm" }],
    "Hồ Chí Minh": [{ value: "Quận 1", label: "Quận 1" }, { value: "Quận 3", label: "Quận 3" }],
  };

  const handleFilterSearch = () => {
    console.log("Tìm kiếm từ bộ lọc:", { selectedProvince, selectedDistrict });
    if (onSearch) {
      onSearch({ selectedProvince, selectedDistrict });
    }
    setFilterVisible(false);
    setSelectedProvince(null);
    setSelectedDistrict(null);
  };

  const handleResetFilters = () => {
    setSelectedProvince(null);
    setSelectedDistrict(null);
    setFilterVisible(false);
    console.log("Xóa bộ lọc", {selectedDistrict,selectedProvince });
  };

  return (
    <Dropdown
      open={filterVisible}
      onOpenChange={setFilterVisible}
      trigger={["click"]}
      overlay={
        <div onClick={(e) => e.stopPropagation()}>
          <Space direction="vertical">
            <Select
              placeholder="Chọn tỉnh/thành"
              style={{ width: "200px" }}
              options={provinces}
              value={selectedProvince}
              onChange={(value) => {
                setSelectedProvince(value);
                setSelectedDistrict(null);
              }}
            />
            <Select
              placeholder="Chọn quận/huyện"
              style={{ width: "200px" }}
              options={selectedProvince ? districts[selectedProvince] : []}
              value={selectedDistrict}
              onChange={setSelectedDistrict}
              disabled={!selectedProvince}
            />
            <Button type="primary" onClick={handleFilterSearch} block>Tìm kiếm</Button>
            <Button onClick={handleResetFilters} block>Xóa bộ lọc</Button>
          </Space>
        </div>
      }
    >
      <Button icon={<FilterOutlined />}>Lọc</Button>
    </Dropdown>
  );
}
