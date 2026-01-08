import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";
import {jwtDecode} from "jwt-decode";

import loginCar from "../assets/car.jpg";
import "./Login.css";

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // CAPTCHA
  const [captcha, setCaptcha] = useState("");
  const [inputCaptcha, setInputCaptcha] = useState("");
  


  // Generate CAPTCHA
  const generateCaptcha = () => {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    let code = "";
    for (let i = 0; i < 6; i++) {
      code += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    setCaptcha(code);
    setInputCaptcha("");
  };

  useEffect(() => {
    generateCaptcha();
  }, []);

  const verifyCaptcha = () => inputCaptcha === captcha;

  const handleLogin = async () => {
    if (!email || !password) {
      alert("Please fill all fields");
      return;
    }

    if (!verifyCaptcha()) {
      alert("Incorrect CAPTCHA. Please try again.");
      generateCaptcha();
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await res.json();
      const safeEmail = data.email || email;
      alert(data.message);
      if (res.ok) 
        {
          localStorage.setItem(
  "user",
  JSON.stringify({
    email: safeEmail,
    name: data.name || safeEmail?.split("@")[0] || "User",
    role: data.role || "BUYER"
  })
);
navigate("/");

        }
    } catch (err) {
      alert("Server error: " + err.message);
    }
  };

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
          localStorage.setItem(
    "user",
    JSON.stringify({
      email: user.email,
      name: user.name,
      role: "BUYER"
    })
  );
      const data = await res.json();
      alert(data.message);
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Google Sign In Failed");
    }
  };

  const handleGoogleError = () => {
    alert("Google Sign In Failed");
  };

  return (
    <div className="login-container">
      <div className="login-card">

        {/* LEFT PANEL */}
        <div className="login-left">
          <div className="semicircle-shape"></div>
          <h1>LOGIN <br /> PAGE</h1>
        </div>

        {/* MIDDLE IMAGE */}
        <div className="login-middle">
          <img src={loginCar} alt="Login Car" />
        </div>

        {/* RIGHT FORM */}
        <div className="login-right">
          <h2>Login</h2>
          <p className="welcome-text">Welcome back! Please enter your details</p>

          <div className="form-group">
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

            {/* CAPTCHA */}
            <div className="captcha-row">
              <div className="captcha-box">
                {captcha} <span className="refresh" onClick={generateCaptcha}>⟳</span>
              </div>
              <input
                type="text"
                placeholder="Enter CAPTCHA"
                value={inputCaptcha}
                onChange={(e) => setInputCaptcha(e.target.value)}
              />
            </div>

            <Link className="forgot-link" to="/forgot-password">
              Forgot Password
            </Link>
          </div>

          <button className="login-btn" onClick={handleLogin}>
            Login
          </button>

          <div className="divider">
            <span></span>
            <p>or</p>
            <span></span>
          </div>

          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={handleGoogleError}
          />

          <p className="signup-redirect">
            Don’t have an account? <Link to="/signup">Sign up for free</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
