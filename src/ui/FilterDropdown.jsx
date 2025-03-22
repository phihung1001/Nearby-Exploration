import React, { useState } from "react";
import { Select, Dropdown, Button, Space } from "antd";
import { FilterOutlined } from "@ant-design/icons";
import provincesData  from '../assets/provinces';
export default function FilterDropdown({ onSearch }) { const [selectedProvinceId, setSelectedProvinceId] = useState(null);
  const [selectedDistrictId, setSelectedDistrictId] = useState(null);
  const [filterVisible, setFilterVisible] = useState(false);

  // Chuyển provincesData thành options
  const provinceOptions = provincesData.map(province => ({
    value: province.Id,
    label: province.Name
  }));

  // Lấy danh sách quận/huyện theo tỉnh đã chọn
  const districts = provincesData.find(p => p.Id === selectedProvinceId)?.Districts || [];

  const districtOptions = districts.map(district => ({
    value: district.Id,
    label: district.Name
  }));

  const handleFilterSearch = () => {
    const selectedProvince = provincesData.find(p => p.Id === selectedProvinceId);
    const selectedDistrict = districts.find(d => d.Id === selectedDistrictId);

    const filterData = {
      provinceId: selectedProvinceId,
      provinceName: selectedProvince?.Name,
      districtId: selectedDistrictId,
      districtName: selectedDistrict?.Name,
    };

    console.log("Tìm kiếm từ bộ lọc:", filterData);

    if (onSearch) {
      onSearch(filterData);
    }
    setFilterVisible(false);
  };

  const handleResetFilters = () => {
    setSelectedProvinceId(null);
    setSelectedDistrictId(null);
    setFilterVisible(false);
    console.log("Xóa bộ lọc");
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
              options={provinceOptions}
              value={selectedProvinceId}
              onChange={(value) => {
                setSelectedProvinceId(value);
                setSelectedDistrictId(null);
              }}
            />
            <Select
              placeholder="Chọn quận/huyện"
              style={{ width: "200px" }}
              options={districtOptions}
              value={selectedDistrictId}
              onChange={setSelectedDistrictId}
              disabled={!selectedProvinceId}
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
