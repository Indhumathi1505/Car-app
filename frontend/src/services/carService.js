// carService.js
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/cars";

export const getAllCars = () =>
  axios.get(`${BASE_URL}/all`);

export const addCar = (formData) =>
  axios.post(`${BASE_URL}/add`, formData);
