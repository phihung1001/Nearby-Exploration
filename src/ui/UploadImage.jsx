import React, { useState, useRef } from "react";
import { Upload, Button, message } from "antd";
import { UploadOutlined, CameraOutlined, PictureOutlined, CloseOutlined, CheckOutlined } from "@ant-design/icons";
import { useNavigate, useLocation } from "react-router-dom";
import Webcam from "react-webcam";

export default function UploadImage({ onUpload = () => {}, onCancel = () => {}, closeModal }) {
  const [fileList, setFileList] = useState([]);
  const [capturedImage, setCapturedImage] = useState(null);
  const [isCameraActive, setIsCameraActive] = useState(false);
  const webcamRef = useRef(null);
  const [data, setData] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();

  /** Xử lý chọn ảnh từ máy */
  const handleUploadImage = ({ fileList }) => {
    setFileList(fileList);
    setCapturedImage(null); 
  };

  /** Xử lý chụp ảnh từ webcam */
  const handleCaptureImage = () => {
    const imageSrc = webcamRef.current.getScreenshot();
    setCapturedImage(imageSrc);
    setIsCameraActive(false);
    setFileList([]);
  };

  /** Xử lý tải ảnh lên */
  const handleUpload = async () => {
    if (!fileList.length && !capturedImage) {
      message.warning("Vui lòng chọn hoặc chụp ảnh trước khi tải lên!");
      return;
    }

    const formData = new FormData();

    if (capturedImage) {
      const blob = await fetch(capturedImage).then(res => res.blob());
      formData.append("file", blob, "captured-image.png");
    } else if (fileList.length > 0) {
      formData.append("file", fileList[0].originFileObj);
    }

    try {
      const token = localStorage.getItem('token');
      const response = await fetch("http://localhost:8080/customer/searchByImage", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Lỗi từ server:", errorText);
        message.error(`Có lỗi khi tải ảnh lên: ${errorText}`);
        return;
      }

      const result = await response.json();

      // Trích xuất dữ liệu cần thiết
      const formattedData = {
        label: result.label,
        top5Restaurants: result.top_5.map(item => ({
          name: item.class_name,
          probability: item.probability
        }))
      };
  
      // Lưu dữ liệu vào state
      setData(formattedData);

      // Đóng modal nếu có hàm đóng modal được truyền vào
      if (closeModal) closeModal();

      // Chuyển hướng về trang Home nếu không ở trang Home
      if (location.pathname !== "/") {
        navigate("/");
      }

      console.log("Data mới nhất: ", formattedData);
      message.success("Tải ảnh lên thành công!");

      // Gọi API lấy thông tin nhà hàng
      const restaurants = await fetchRestaurants(result.label);

      // Điều hướng đến trang mới và truyền dữ liệu nhà hàng qua state
      navigate("/public/restaurant-list", { state: { restaurants } });

    } catch (error) {
      console.error("Lỗi trong quá trình tải ảnh lên:", error);
      message.error("Có lỗi xảy ra khi tải ảnh lên!");
    }
  };

  /** Hàm gọi API lấy thông tin nhà hàng */
  const fetchRestaurants = async (name) => {
    try {
      const response = await fetch(`http://localhost:8080/public/restaurant/filter?name=${name}`);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const restaurants = await response.json();
      return restaurants; // Trả về danh sách nhà hàng
    } catch (error) {
      console.error("Có lỗi xảy ra khi lấy thông tin nhà hàng:", error);
      return [];
    }
  };

  /** Xử lý xóa ảnh */
  const handleRemoveImage = () => {
    setFileList([]);
    setCapturedImage(null);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "10px", alignItems: "center" }}>
      {!isCameraActive && !capturedImage && fileList.length === 0 && (
        <>
          <Button
            style={{ width: "160px", height: "32px" }}
            type="primary"
            icon={<CameraOutlined />}
            onClick={() => setIsCameraActive(true)}
          >
            Chụp ảnh
          </Button>

          <Upload
            fileList={fileList}
            beforeUpload={() => false}
            onChange={handleUploadImage}
            accept="image/*"
            maxCount={1}
          >
            <Button style={{ width: "160px", height: "32px" }} icon={<UploadOutlined />}>
              Chọn ảnh từ máy
            </Button>
          </Upload>
        </>
      )}

      {isCameraActive && (
        <>
          <Webcam ref={webcamRef} screenshotFormat="image/png" width={250} height={200} />
          <div style={{ display: "flex", gap: "10px" }}>
            <Button type="primary" icon={<PictureOutlined />} onClick={handleCaptureImage}>
              Chụp ngay
            </Button>
            <Button icon={<CloseOutlined />} onClick={() => setIsCameraActive(false)}>
              Hủy
            </Button>
          </div>
        </>
      )}

      {(capturedImage || fileList.length > 0) && (
        <>
          {capturedImage && (
            <img src={capturedImage} alt="Ảnh chụp" style={{ width: "100%", borderRadius: "8px" }} />
          )}

          {fileList.length > 0 && (
            <img
              src={URL.createObjectURL(fileList[0].originFileObj)}
              alt="Ảnh đã chọn"
              style={{ width: "100%", borderRadius: "8px" }}
            />
          )}

          <div style={{ display: "flex", gap: "10px" }}>
            <Button type="primary" onClick={handleUpload} icon={<CheckOutlined />}>
              Tải ảnh lên
            </Button>
            <Button danger onClick={handleRemoveImage} icon={<CloseOutlined />}>
              Xóa ảnh
            </Button>
          </div>
        </>
      )}
    </div>
  );
}