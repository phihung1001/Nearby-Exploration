import React, { useState } from "react";
import { Spin } from "antd";
import { useParams } from "react-router-dom";
import { useUserData } from "../../../hooks/useUserData";
import ProfileLayout from "./ProfileLayout";
import InfoPanel from "./InfoPanel";
import ChangePasswordPanel from "./ChangePasswordPanel";
import ProviderPanel from "./ProviderPanel";
import urlImage from "../../../assets/Img/bground4.jpg";
import EditRestaurantPanel from "./EditRestaurantPanel";
import AddRestaurantPanel from "./AddRestaurantPanel";
import DishManager from "./DishesManagement";

export default function Profile() {
  const { id } = useParams();
  const { userData, loading, error } = useUserData(id);
  const [localUserData, setLocalUserData] = useState(userData);
  const handleUpdateSuccess = (updatedData) => {
    setLocalUserData(updatedData);
  };

  const [selectedMenu, setSelectedMenu] = useState({ menu: "info", restaurant: null });

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

  // Xác định nội dung bên phải dựa trên selectedMenu
  let rightContent;
  if (selectedMenu.menu === "info") {
    rightContent = <InfoPanel userData={displayedData} onUpdateSuccess={handleUpdateSuccess} />
  } else if (selectedMenu.menu === "changePassword") {
    rightContent = <ChangePasswordPanel id={displayedData.id} />;
  } else if (selectedMenu.menu === "changeAvatar") {
    rightContent = <p>Cập nhật hình đại diện</p>;
  } else if (selectedMenu.menu === "restaurantManagement") {
    rightContent = <ProviderPanel onSelectMenu={setSelectedMenu} />;
  } else if( selectedMenu.menu === "addRestaurant") {
    rightContent = (
      <AddRestaurantPanel 
       onCancel={() => setSelectedMenu({ menu: "restaurantManagement" })}
      />
    ) 
  } else if (selectedMenu.menu === "editRestaurant") {
    rightContent = (
      <EditRestaurantPanel
        restaurantData={selectedMenu.restaurant}
        onCancel={() => setSelectedMenu({ menu: "restaurantManagement" })}
      />
    );
  } else if (selectedMenu.menu === "dishesManagement") {
    rightContent = <DishManager />
  } else if (selectedMenu.menu === "logout") {
    localStorage.removeItem("token");
    window.location.href = "/";
  }


  return (
    <div style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      minHeight: "100vh",
      backgroundImage: "url('" + urlImage + "')",
      backgroundRepeat: "no-repeat",
      backgroundSize: "cover",
    }}>
        <ProfileLayout
          userData={userData}
          selectedMenu={selectedMenu}
          onSelectMenu={setSelectedMenu}
          rightContent={rightContent}
        />
    </div>
    
  );
}
