import React, { useState } from "react";
import { Progress, Button, message, Modal, Form, Input, Rate } from "antd";
import styles from "./ReviewSummary.module.css";
import { postComment } from "../../services/commentService";

const ReviewSummary = ({ ratings, restaurantId, onCommentSuccess }) => {
  const token = localStorage.getItem("token");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form] = Form.useForm();

  const handleSubmit = async (values) => {
    const commentData = {
      comment: values.comment,
      avgRatingText: values.avgRatingText * 2,
    };
    try {
      await postComment(restaurantId, commentData, token);
      message.success("Bình luận đã được gửi!");
      setIsModalOpen(false);
      form.resetFields();
      onCommentSuccess?.();
    } catch {
      message.error("Gửi bình luận thất bại.");
    }
  };

  return (
    <div className={styles.reviewContainer}>
      {/* ... Phần hiển thị tổng quan đánh giá ... */}

      <div className={styles.progressContainer}>
        {ratings.criteria.map((item, index) => (
          <div key={index}>
            <div className={styles.progressLabel}>
              <span>{item.label}</span>
              <span>{item.value}</span>
            </div>
            <Progress percent={item.value * 10} showInfo={false} strokeColor="#3498db" />
          </div>
        ))}
      </div>

      <div className={styles.ratingScore}>{ratings.overall} điểm - Khá tốt</div>

      <Button className={styles.writeCommentBtn} onClick={() => setIsModalOpen(true)}>
        Viết bình luận
      </Button>

      <Modal
        title="Viết bình luận"
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        onOk={() => form.submit()}
        okText="Gửi"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Form.Item
            name="comment"
            label="Nội dung bình luận"
            rules={[{ required: true, message: "Vui lòng nhập bình luận!" }]}
          >
            <Input.TextArea rows={4} placeholder="Nhập cảm nhận của bạn..." />
          </Form.Item>

          <Form.Item
            name="avgRatingText"
            label="Đánh giá tổng thể (từ 0 đến 10, mỗi sao tương đương 2 điểm)"
            rules={[{ required: true, message: "Vui lòng chọn điểm!" }]}
          >
            <Rate allowHalf />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ReviewSummary;
