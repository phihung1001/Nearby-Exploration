const BASE_URL = 'http://localhost:8080/public/restaurant'
  
  // api lấy danh sách nhà hàng đã lưu phân trang
  export async function getSavedRestaurants(page, token) {
    const response = await fetch(`http://localhost:8080/customer/restaurant-save/list?page=${page}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  
    const data = await response.json();
  
    if (!response.ok) {
      throw new Error(data.message || "Có lỗi xảy ra khi lấy danh sách nhà hàng.");
    }
  
    return data;
  }
  
  // api lưu nhà hàng vào mục yêu thích
  export async function saveRestaurant(restaurant, token) {
    const response = await fetch(`${BASE_URL}/save/${restaurant.id}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(restaurant),
    });
  
    const data = await response.json();
  
    if (!response.ok) {
      throw new Error(data.message || "Không thể lưu nhà hàng.");
    }
    return data;
  }

  // api fetch danh sách nhà hàng gần vị trí hiện tại
  export async function fetchNearbyRestaurantsAPI({ latitude, longitude, page }) {
    const response = await fetch(
      `${BASE_URL}/nearby?latitude=${latitude}&longitude=${longitude}&page=${page}`
    );
  
    const data = await response.json();
  
    if (!response.ok) {
      throw new Error(data.message || "Không thể lấy danh sách nhà hàng gần bạn.");
    }
  
    return data;
  }
  
  // api đăng ký nhà hàng mới
  export async function registerRestaurantAPI(data, token) {
    const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });
  
    const result = await response.json();
  
    if (!response.ok) {
      throw new Error(result.message || "Đăng ký nhà hàng thất bại.");
    }
  
    return result;
  }

  // api update nhà hàng
  export async function updateRestaurantAPI(id, data, token) {
    const response = await fetch(`${BASE_URL}/update/${id}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });
  
    const result = await response.json();
  
    if (!response.ok) {
      throw new Error(result.message || "Cập nhật nhà hàng thất bại.");
    }
  
    return result;
  }
  
  // api trả về danh sách nhà hàng gần vị trí hiện tại theo từ khóa
  export async function searchNearbyRestaurantsByKeywords(keywords, latitude, longitude) {
    let allRestaurants = [];
  
    for (const keyword of keywords) {
      const response = await fetch(
        `${BASE_URL}/nearby?name=${encodeURIComponent(keyword)}&latitude=${latitude}&longitude=${longitude}`
      );
  
      if (!response.ok) {
        throw new Error(`API lỗi: ${response.status}`);
      }
  
      const result = await response.json();
      if (Array.isArray(result.content)) {
        allRestaurants = [...allRestaurants, ...result.content];
      }
    }
  
    return allRestaurants;
  }

    // api lấy lấy thông tin nhà hàng
    export async function getRestaurantById(id) {
        try {
            const response = await fetch(`${BASE_URL}/${id}`);
            const data = await response.json();
            if (response.ok) {
            return data;
        } else {
          throw new Error(data.message || "Không tìm thấy nhà hàng.");
        }
      } catch (error) {
        console.error("Lỗi khi gọi API lấy nhà hàng:", error);
        throw error;
      }
    }

    // api lấy all món ăn của nhà hàng
    export const getDishesByRestaurantId = async (restaurantId) => {
      try {
        const response = await fetch(`http://localhost:8080/public/dishes/get-all-dishes/${restaurantId}`);
        if (!response.ok) {
          throw new Error("Không thể tải dữ liệu món ăn");
        }
        return await response.json();
      } catch (error) {
        console.error("Lỗi khi gọi API:", error);
        throw error;
      }
    };