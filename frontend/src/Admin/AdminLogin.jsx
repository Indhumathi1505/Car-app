import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AdminLogin.css";

export default function AdminLogin() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    if (username === "cartrizo" && password === "123") {
        localStorage.setItem("token", "ADMIN_DUMMY_TOKEN");
  localStorage.setItem("role", "ADMIN");

      navigate("/admin/pending"); // ✅ redirect here
    } else {
      alert("Invalid admin credentials");
    }
  };

  return (
    <div className="admin-container">
      <div className="admin-card">
        <h2 className="admin-title">Admin Login</h2>

        <form onSubmit={handleSubmit}>
          <div className="admin-field">
            <label>Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div className="admin-field">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="admin-btn">
            Login
          </button>
        </form>
      </div>
    </div>
  );
}
