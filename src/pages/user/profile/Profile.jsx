import React, { useState } from "react";
import { Spin } from "antd";
import { useParams } from "react-router-dom";
import { useUserData } from "../../../hooks/useUserData";
import ProfileLayout from "./ProfileLayout";
import InfoPanel from "./InfoPanel";
import ChangePasswordPanel from "./ChangePasswordPanel";

export default function Profile() {
  const { id } = useParams();
  const { userData, loading, error } = useUserData(id);
  const [localUserData, setLocalUserData] = useState(userData);
  const handleUpdateSuccess = (updatedData) => {
    setLocalUserData(updatedData);
  };

  const [selectedMenu, setSelectedMenu] = useState("info");

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: "2rem", textAlign: "center" }} />;
  }
  if (error) {
    return <p style={{ color: "red" }}>Lỗi: {error}</p>;
  }
  if (!userData) {
    return <p style={{ color: "red" }}>Không tìm thấy người dùng.</p>;
  }
  const displayedData = localUserData || userData;
  console.log("Profile -> displayedData", displayedData)

  // Xác định nội dung bên phải dựa trên selectedMenu
  let rightContent;
  if (selectedMenu === "info") {
    rightContent = <InfoPanel userData={displayedData} onUpdateSuccess={handleUpdateSuccess} />
  } else if (selectedMenu === "changePassword") {
    rightContent = <ChangePasswordPanel id={displayedData.id}/>;
  } else if (selectedMenu === "logout") {
    localStorage.removeItem("token");
    window.location.href = "/";
  }

  return (
    <ProfileLayout
      userData={userData}
      selectedMenu={selectedMenu}
      onSelectMenu={setSelectedMenu}
      rightContent={rightContent}
    />
  );
}
