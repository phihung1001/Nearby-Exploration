// src/pages/Home/Home.js
import React, { useState } from 'react';
import Header from '../../../components/header/Header';
import Banner from '../../../components/home-baner/Banner';
import Explore from '../../../ui/explore/Explore';
import khamphaimg from '../../../assets/Img/loginpage1.jpg';
import styles from './Home.module.css';
import Footer from '../../../components/footer/Footer';
import HomeBodyRightContainer from '../home-body/HomeBodyRightContainer';
import { Button } from 'antd';
import { MessageOutlined } from '@ant-design/icons';
import ChatBot from '../../common/chat-bot/ChatBot';
import { sendMessageToAI } from '../../../api/chatService';  // gọi API trả lời AI

export default function Home() {
  const [isChatVisible, setIsChatVisible] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  const toggleChatForm = () => {
    setIsChatVisible(!isChatVisible);
  };

  const handleSendMessage = async (message) => {
    if (!message.trim()) return;

    const userMsg = { sender: "user", text: message };
    setMessages((prev) => [...prev, userMsg]);
    setInput("");

    // Gọi API tới AI
    try {
      const reply = await sendMessageToAI(message);
      const botMsg = { sender: "bot", text: reply };
      setMessages((prev) => [...prev, botMsg]);
    } catch (err) {
      const errorMsg = { sender: "bot", text: "⚠️ Có lỗi khi kết nối AI!" };
      setMessages((prev) => [...prev, errorMsg]);
    }
  };

  return (
    <div className={styles.homeContainer}>
      <Header />
      <Banner />
      <div className={styles.homeBody}>
        <div className={styles.stickyExplore}>
          <Explore imageUrl={khamphaimg} width="200px" height="300px" />
        </div>
        <div className={styles.homeBodyRightContainer}>
          <HomeBodyRightContainer />
        </div>
      </div>
      <Footer />

      <Button
        type="primary"
        onClick={toggleChatForm}
        style={{
          position: 'fixed',
          bottom: '20px',
          right: '20px',
          zIndex: 1000,
          borderRadius: '70%',
          padding: '15px 20px',
        }}
      >
        <MessageOutlined /> AI
      </Button>
      

      {isChatVisible && (
        <ChatBot
          messages={messages}
          input={input}
          setInput={setInput}
          onSendMessage={handleSendMessage}
          onClose={() => setIsChatVisible(false)}
        />
      )}
    </div>
  );
}
