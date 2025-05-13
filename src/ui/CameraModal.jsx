import React from "react";
import { Modal } from "antd";
import UploadImage from "./UploadImage";

export default function CameraModal({ open, onCancel, onUpload }) {
  const handleCloseModal = () => {
    if (onCancel) onCancel(); // Đảm bảo modal sẽ đóng khi hàm được gọi
  };
  return (
    <Modal title="Tìm kiếm hình ảnh nhanh chóng" open={open} onCancel={onCancel} footer={null} >
      <UploadImage 
        onUpload={onUpload} 
        onCancel={onCancel}  
        closeModal={handleCloseModal} 
      />
    </Modal>
  );
}
