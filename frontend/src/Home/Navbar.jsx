// src/Home/Navbar.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import "./Navbar.css"; // your CSS file for styling

export default function Navbar({ toggleMenu }) {
  const navigate = useNavigate();

  return (
    <header className="topbar">
      <div className="left-group">
        <button className="hamburger" onClick={toggleMenu}>
          <span></span>
          <span></span>
          <span></span>
        </button>
        <div className="brand">
          <img src={logo} alt="logo" className="brand-logo" />
          <h1 className="brand-title">CARTRIZO</h1>
        </div>
      </div>
      <nav className="topnav">
        <button className="topnav-n" onClick={() => navigate("/")}>HOME</button>
        <button className="topnav-n" onClick={() => navigate("/about-us")}>ABOUT US</button>
        <button className="topnav-n" onClick={() => navigate("/signup")}>SIGNUP</button>
        <button className="topnav-n" onClick={() => navigate("/login")}>LOGIN</button>
        <button className="topnav-n" onClick={() => navigate("/profile")}>PROFILE</button>
        {/* Admin button */}
        <button className="topnav-n" onClick={() => navigate("/admin/login")}>ADMIN</button>
      </nav>
    </header>
  );
}
