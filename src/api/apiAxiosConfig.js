import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, 
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const { data } = await axios.post(
          "http://localhost:8080/auth/refresh",
          {},
          { withCredentials: true }
        );
        localStorage.setItem("accessToken", data.accessToken);
        originalRequest.headers["Authorization"] = "Bearer " + data.accessToken;
        return api(originalRequest);
      } catch (refreshError) {
        window.location.href = "/auth/signin";
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);
export default api;
