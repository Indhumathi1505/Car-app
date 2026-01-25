import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "https://car-backend-final.onrender.com/",
  withCredentials: true
});

// Add a request interceptor to include the JWT token
api.interceptors.request.use((config) => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (user && user.token) {
    config.headers.Authorization = `Bearer ${user.token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default api;
