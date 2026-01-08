import React, { useState } from "react";
import "./info.css";

export default function ProfileInfo() {
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    gender: "",
    state: "",
    city: "",
    pincode: "",
    address: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/profile", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form)
      });

      if (!res.ok) throw new Error("API failed");

      alert("Profile saved successfully!");
         
    } catch (error) {
      console.error(error);
      alert("Backend not running or CORS issue");
    }
  };

  return (
    <div className="info-page">
      <div className="info-card">

        <h2 className="info-title">Fill your information</h2>

        <div className="form-group">
          <label>Full Name</label>
          <input name="fullName" value={form.fullName} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Email</label>
          <input name="email" value={form.email} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Phone</label>
          <input name="phone" value={form.phone} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Gender</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="gender"
                value="Male"
                checked={form.gender === "Male"}
                onChange={handleChange}
              />
              Male
            </label>

            <label>
              <input
                type="radio"
                name="gender"
                value="Female"
                checked={form.gender === "Female"}
                onChange={handleChange}
              />
              Female
            </label>
          </div>
        </div>

        <div className="form-group">
          <label>State</label>
          <input name="state" value={form.state} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>City</label>
          <input name="city" value={form.city} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Pincode</label>
          <input name="pincode" value={form.pincode} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Address</label>
          <textarea
            name="address"
            value={form.address}
            onChange={handleChange}
          />
        </div>

        <button className="save-btn" onClick={handleSubmit}>
          
          Save & Continue
        </button>

      </div>
    </div>
  );
}