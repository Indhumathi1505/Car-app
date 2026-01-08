import { useState } from "react";
import api from "../api/api";
import { useNavigate } from "react-router-dom";
import "../pages/UploadCar.css";


export default function UploadCar() {
  const showroom = JSON.parse(localStorage.getItem("showroom"));
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title:"", brand:"", price:"", year:"", description:""
  });
  const [images, setImages] = useState([]);

  const upload = async () => {
    const data = new FormData();
    Object.keys(form).forEach(k => data.append(k, form[k]));
    data.append("showroomId", showroom.id);

    for (let img of images) data.append("images", img);

    await api.post("/cars/upload", data);
    navigate("/showroom/dashboard");
  };

  return (
    <div>
      <h2>Upload Car</h2>
      <input placeholder="Title" onChange={e=>setForm({...form,title:e.target.value})}/>
      <input placeholder="Brand" onChange={e=>setForm({...form,brand:e.target.value})}/>
      <input placeholder="Price" onChange={e=>setForm({...form,price:e.target.value})}/>
      <input placeholder="Year" onChange={e=>setForm({...form,year:e.target.value})}/>
      <textarea placeholder="Description" onChange={e=>setForm({...form,description:e.target.value})}/>
      <input type="file" multiple onChange={e=>setImages(e.target.files)} />
      <button onClick={upload}>Upload</button>
    </div>
  );
}
