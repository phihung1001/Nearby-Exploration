import { useEffect, useRef } from "react";
import styles from "./ChatBot.module.css";
import { MessageOutlined } from '@ant-design/icons';

function ChatBot({ messages, input, setInput, onSendMessage, onClose }) {
  const chatEndRef = useRef(null);

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    onSendMessage(input);
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend(e);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <span><MessageOutlined /> Trợ lý AI</span>
        <button className={styles.closeButton} onClick={onClose}>×</button>
      </div>

      <form className={styles.chatForm} onSubmit={handleSend}>
        <div className={styles.chatBox}>
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`${msg.sender === "user" ? styles.userWrapper : styles.botWrapper} ${styles.messageWrapper}`}
            >
              <div className={`${msg.sender === "user" ? styles.user : styles.bot} ${styles.message}`}>
                {msg.text}
              </div>
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>

        <div className={styles.inputGroup}>
          <textarea
            placeholder="Nhập tin nhắn..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={handleKeyPress}
            className={styles.input}
            rows={3}
          />
          <button type="submit" className={styles.button}>Gửi</button>
        </div>
      </form>
    </div>
  );
}

export default ChatBot;
