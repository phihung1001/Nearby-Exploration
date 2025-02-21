import React from "react";
import { Modal } from "antd";
import UploadImage from "./UploadImage";

export default function CameraModal({ open, onCancel, onUpload }) {
  return (
    <Modal title="Tìm kiếm hình ảnh nhanh chóng" open={open} onCancel={onCancel} footer={null} destroyOnClose>
      <UploadImage onUpload={onUpload} onCancel={onCancel}  />
    </Modal>
  );
}
