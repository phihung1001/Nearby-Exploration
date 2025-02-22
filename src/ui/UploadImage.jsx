import React, { useState, useRef } from "react";
import { Upload, Button, message } from "antd";
import { UploadOutlined, CameraOutlined, PictureOutlined, CheckOutlined } from "@ant-design/icons";
import Webcam from "react-webcam";

export default function UploadImage({ onUpload = () => {},onCancel }) {  // Đảm bảo `onUpload` có giá trị mặc định
  const [fileList, setFileList] = useState([]);
  const [capturedImage, setCapturedImage] = useState(null);
  const [isCameraActive, setIsCameraActive] = useState(false);
  const webcamRef = useRef(null);

  /** Xử lý upload ảnh từ máy */
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
  const handleUpload = () => {
    if (!fileList.length && !capturedImage) {
      message.warning("Vui lòng chọn hoặc chụp ảnh trước khi tải lên!");
      return;
    }

    if (capturedImage) {
      onUpload(capturedImage);  //  Kiểm tra onUpload trước khi gọi
    } else if (fileList.length > 0) {
      onUpload(fileList[0].originFileObj);
    }

    console.log("anh chup: ",capturedImage );
    console.log('anh upload: ', fileList );
    onCancel();
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "10px", alignItems: "center" }}>
        {!isCameraActive && !capturedImage && (
          <>
            <Button style={{ width: "160px" ,height:"32px" }} type="primary" icon={<CameraOutlined />} onClick={() => setIsCameraActive(true)}>
              Chụp ảnh
            </Button>

            <Upload fileList={fileList} beforeUpload={() => false} onChange={handleUploadImage} accept="image/*">
              <Button style={{ width: "160px" ,height:"32px" }} icon={<UploadOutlined />}>Chọn ảnh từ máy</Button>
            </Upload>
          </>
        )}

      {isCameraActive && (
        <>
          <Webcam ref={webcamRef} screenshotFormat="image/png" width={250} height={200} />
          <Button type="primary" icon={<PictureOutlined />} onClick={handleCaptureImage}>
            Chụp ngay
          </Button>
        </>
      )}

      {capturedImage && (
        <>
          <img src={capturedImage} alt="Ảnh chụp" style={{ width: "100%", borderRadius: "8px" }} />
        </>
      )}

      {(fileList.length > 0 || capturedImage) && (
        <Button type="primary" onClick={handleUpload}>
          Tải ảnh lên
        </Button>
      )}
    </div>
  );
}
