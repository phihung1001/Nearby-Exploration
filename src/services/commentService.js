import axios from "axios";


const API_URL = "http://localhost:8080/public/restaurant"; 

// API lấy tất cả bình luận của nhà hàng theo id
export const getCommentsByRestaurantId = async (id, page, size) => {
  const response = await axios.get(`${API_URL}/get-all-comment/${id}`,
     {
        params: { page, size }
      }
  );
  return response.data; 
};

//API tạo bình luận cho nhà hàng
// restaurantId: id của nhà hàng
export const postComment = async (restaurantId, commentData, token) => {
  try {
    const response = await axios.post(
      `http://localhost:8080/customer/comment/${restaurantId}`,
      commentData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Lỗi gửi bình luận:", error);
    throw error;
  }
};