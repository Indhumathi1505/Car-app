import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";
import {jwtDecode} from "jwt-decode";

import carImg from "../assets/car.jpg";
import "./Signup.css";

export default function Signup() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // NORMAL SIGNUP
  const handleSignup = async () => {
    if (!name || !email || !password) {
      alert("Please fill all fields");
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password }),
      });

      const data = await res.json();
      alert(data.message);

      if (res.ok) navigate("/info");
    } catch (err) {
      alert("Server error: " + err.message);
    }
  };

  // GOOGLE SIGNUP
  const handleGoogleSuccess = async (response) => {
    try {
      const user = jwtDecode(response.credential);

      const res = await fetch("http://localhost:8080/google-login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: user.name,
          email: user.email,
        }),
      });

      const data = await res.json();
      alert(data.message);
      navigate("/info");
    } catch (err) {
      console.error(err);
      alert("Google Sign In Failed");
    }
  };

  const handleGoogleError = () => {
    alert("Google Sign In Failed");
  };

  return (
    <div className="signup-container">
      <div className="signup-card">

        {/* LEFT PANEL */}
        <div className="signup-left">
          <div className="semicircle"></div>
          <img src={carImg} alt="Car" className="car-image" />
          <div className="left-text">
            <h1>SIGNUP</h1>
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="signup-right">
          <h2>Create Account</h2>
          <p className="subtitle">Register to explore our website</p>

          <div className="form-group">
            <input
              type="text"
              placeholder="Username"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button className="signup-btn" onClick={handleSignup}>
            Signup
          </button>

          <div className="divider">
            <span></span>
            <p>or</p>
            <span></span>
          </div>

          {/* GOOGLE LOGIN */}
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={handleGoogleError}
          />

          <p className="login-text">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
