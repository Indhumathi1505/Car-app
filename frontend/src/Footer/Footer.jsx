import React from "react";
import "./Footer.css";
import carImg from "../Footer/footer-car.avif"; // use any car image

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">

        {/* LEFT */}
        <div className="footer-left">
          <h3 className="footer-title">CONTACT WITH US</h3>

          <div className="social-icons">
            <span>🌐</span>
            <span>📘</span>
            <span>📸</span>
            <span>▶</span>
            <span>in</span>
          </div>

          <p className="footer-text">TOLL FREE: 0806 8441 441</p>
          <p className="footer-text">EMAIL: https@gmail.com</p>
        </div>

        {/* CENTER */}
        <div className="footer-center">
          <ul>
            <li>Home</li>
            <li>About us</li>
            <li>New cars</li>
            <li>Used cars</li>
            <li>Sell cars</li>
            <li>Rate us</li>
          </ul>
        </div>

        {/* RIGHT */}
        <div className="footer-right">
          <img src={carImg} alt="car" />
          <p className="footer-quote">
            “YOUR NEXT ADVENTURE <br />
            STARTS WITH THE RIGHT <br />
            WHEELS.”
          </p>
        </div>

      </div>

      {/* BOTTOM */}
      <div className="footer-bottom">
        © 2024 CARTRIZO. ALL RIGHTS RESERVED
      </div>
    </footer>
  );
}
