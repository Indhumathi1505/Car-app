import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./SellerLogin.css";

const SellerSignin = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [captcha, setCaptcha] = useState("");
  const [inputCaptcha, setInputCaptcha] = useState("");
  const [error, setError] = useState("");
  

  const generateCaptcha = () =>
    Math.floor(1000 + Math.random() * 9000).toString();

  useEffect(() => {
    setCaptcha(generateCaptcha());
  }, []);

 const handleSubmit = (e) => {
  e.preventDefault();

  if (!username || !password || !inputCaptcha) {
    setError("All fields are required");
    return;
  }

  if (inputCaptcha !== captcha) {
    setError("Captcha is incorrect");
    setCaptcha(generateCaptcha());
    setInputCaptcha("");
    return;
  }

  setError("");

  // ✅ SAVE EMAIL AFTER SUCCESS
  localStorage.setItem("showroomEmail", username);

  // ✅ Navigate
  navigate("/sell-role");
};


  return (
    <div className="main">
    <div className="container">
      <h2>LOGIN</h2>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Seller Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <div className="captcha-container">
          <span>{captcha}</span>
          <button
            type="button"
            className="refresh-btn"
            onClick={() => setCaptcha(generateCaptcha())}
          >
            🔄
          </button>
        </div>

        <input
          type="text"
          placeholder="Enter Captcha"
          value={inputCaptcha}
          onChange={(e) => setInputCaptcha(e.target.value)}
        />

        {error && <p className="error">{error}</p>}

        <button type="submit">Log In</button>
      </form>
    </div>
    </div>
  );
};

export default SellerSignin;
