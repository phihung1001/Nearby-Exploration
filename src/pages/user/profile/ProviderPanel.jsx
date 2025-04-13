import React, { useEffect, useState } from "react";
import { Button, Spin, notification } from "antd";
import { jwtDecode } from 'jwt-decode';

export default function ProviderPanel({ onSelectMenu }) {
  const [providers, setProviders] = useState([]);
  const [loading, setLoading] = useState(true);
  let roles = [];

  useEffect(() => {
    const fetchData = async () => {
        const token = localStorage.getItem("token");
        if (token) {
            const decoded = jwtDecode(token);
            roles = decoded.roles || [];
          }
        const response = await fetch("http://localhost:8080/public/restaurant/list-restaurant/user", {
          headers: { Authorization: `Bearer ${token}` }
        });
        const res = await response.json();

        if (!response.ok) {
            setLoading(false);
            return;
        } else {
            setProviders(res.content);
            console.log("data", res.content);
            setLoading(false);
        }
     
    };
  
    fetchData();
  }, []);
  

  const handleEdit = (restaurant) => {
    console.log("restaurant",restaurant);
    onSelectMenu({ menu: "editRestaurant", restaurant });
  }

  if (loading) {
    return <Spin size="large" style={{ display: "block", marginTop: "2rem", textAlign: "center" }} />;
  }

  const handleAddRestaurant = () => {
    onSelectMenu({ menu: "addRestaurant" });
  }

  const handleRequestUpgrade = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("http://localhost:8080/customer/upgrade-provider", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        }
      });
  
      const res = await response.json();
  
      if (!response.ok) {
        throw new Error(res.message || "Nâng cấp thất bại!");
      }
  
      notification.success({
        message: "Thành công",
        description: "Tài khoản của bạn đã được nâng cấp. Vui lòng đăng nhập lại!"
      });
  
      // Sau khi upgrade có thể logout hoặc refresh token
      localStorage.removeItem("token");
      window.location.reload();
  
    } catch (error) {
      notification.error({
        message: "Thất bại",
        description: error.message
      });
    }
  };
  
  

  return (
    <div style={{ padding: "1rem" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h2>Nhà hàng của bạn</h2>
        {roles.includes("PROVIDER") ? (<Button type="primary" onClick={handleAddRestaurant}>+ Thêm Nhà Hàng</Button>) : (
            <Button type="primary" onClick={handleRequestUpgrade}>Yêu cầu cấp quyền đăng kí nhà hàng</Button>
        )}

      </div>

      <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
      {providers.length === 0 ? (
          <p>Bạn chưa có nhà hàng nào.</p>
        ) : (
            <ul style={{ listStyle: "none", padding: 0 }}>
            {providers.map(restaurant => (
              <li key={restaurant.id} style={{ background: "#fff", padding: "16px", borderRadius: "8px", boxShadow: "0 2px 8px rgba(0,0,0,0.05)", marginBottom: "12px" }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <div>
                    <h3 style={{ margin: 0 }}>{restaurant.name}</h3>
                    <p style={{ margin: "4px 0 0 0", color: "#666" }}>{restaurant.address}</p>
                  </div>
                  <Button type="primary" onClick={() => handleEdit(restaurant)}>Chỉnh sửa</Button>
                </div>
              </li>
            ))}
          </ul>
          
        )}
      </div>
    </div>
  );
}
