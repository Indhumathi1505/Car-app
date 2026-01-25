// carService.js
import axios from "axios";

const BASE_URL = "https://car-backend-final.onrender.com/api/cars";

export const getAllCars = () =>
  axios.get(`${BASE_URL}/all`);

export const addCar = (formData) =>
  axios.post(`${BASE_URL}/add`, formData);
