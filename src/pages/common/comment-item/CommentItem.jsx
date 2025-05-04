import React from "react";
import styles from "./CommentItem.module.css";
import { LikeOutlined, MessageOutlined } from "@ant-design/icons";

const CommentItem = ({ avatar, username, date, content, rating }) => {
  return (
    <div className={styles.commentContainer}>
      <div className={styles.commentHeader}>
        <img src={avatar} alt="avatar" className={styles.avatar} />
        <div className={styles.userInfo}>
          <span className={styles.username}>{username}</span>
          <span className={styles.commentDate}>
            {date}
          </span>
        </div>
        <span className={styles.rating}>{rating}</span>
      </div>
      <div className={styles.commentBody}>
        <p className={styles.commentContent}>{content}</p>
      </div>
      <div className={styles.commentActions}>
        <span className={styles.actionItem}>
          <LikeOutlined /> Thích
        </span>
        <span className={styles.actionItem}>
          <MessageOutlined /> Thảo luận
        </span>
      </div>
    </div>
  );
};

export default CommentItem;
