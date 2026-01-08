import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/cars", // include /cars here
});

export default api;
