import React from "react";
import { Card, Button } from "antd";

export default function ItemRestaurantCard({ restaurant, onEdit }) {
  return (
    <Card
      hoverable
      style={{ width: 300, marginBottom: 16 }}
      cover={
        restaurant.image ? (
          <img alt={restaurant.name} src={restaurant.image} style={{ height: 180, objectFit: "cover" }} />
        ) : (
          <div style={{
            height: 180,
            background: "#f0f0f0",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            color: "#999"
          }}>
            Chưa có ảnh
          </div>
        )
      }
    >
      <Card.Meta
        title={restaurant.name}
        description={restaurant.address}
      />
      <div style={{ marginTop: 12, display: "flex", justifyContent: "flex-end" }}>
        <Button type="primary" onClick={() => onEdit(restaurant.id)}>
          Chỉnh sửa
        </Button>
      </div>
    </Card>
  );
}
