export const changePassword = async (oldPassword, newPassword) => {
    const token = localStorage.getItem("token");
  
    const response = await fetch("http://localhost:8080/auth/change-password", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ oldPassword, newPassword })
    });
  
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || `Lỗi ${response.status}`);
    }
  
    return response.json();
  };
  
  // API đăng nhập tài khoảnkhoản
  export async function loginUser(credentials) {
    try {
      const response = await fetch('http://localhost:8080/auth/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
      });
  
      const data = await response.json();
  
      if (!response.ok) {
        throw new Error(data.message || 'Đăng nhập thất bại');
      }
  
      return data;
    } catch (error) {
      throw error;
    }
  }

  // API Đăng ký người dùng
  export async function registerUser(userData) {
    try {
      const response = await fetch('http://localhost:8080/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });
  
      const result = await response.json();
  
      if (!response.ok) {
        throw new Error(result.message || 'Đăng ký thất bại');
      }
  
      return result;
    } catch (error) {
      throw error;
    }
  }