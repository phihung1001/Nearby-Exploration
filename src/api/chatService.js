// src/api/chatService.js
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/openai";

export const sendMessageToAI = async (message) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/chat`, { message });
    return response.data.reply;
  } catch (error) {
    console.error("Lỗi khi gọi API chat:", error);
    return "Bot không phản hồi. Vui lòng thử lại.";
  }
};
