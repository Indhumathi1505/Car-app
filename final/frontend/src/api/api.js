import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true   // 🔥 THIS IS REQUIRED
});

export default api;
