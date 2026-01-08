import React, { useState } from "react";
import "./Rateus.css";

export default function RateUs() {
  const [rating, setRating] = useState(0);

  return (
    <div className="rate-container">
      <div className="rate-card">

        <h1 className="rate-title">Rate us</h1>

        <p className="rate-question">
          How was your car purchasing experience with<br />
          Cartrizo?
        </p>

        <div className="stars">
          {[1, 2, 3, 4, 5].map((star) => (
            <span
              key={star}
              className={star <= rating ? "star filled" : "star"}
              onClick={() => setRating(star)}
            >
              ★
            </span>
          ))}
        </div>

        <p className="rate-desc">
          Help us shift gears in the right direction.<br />
          Your feedback keeps us moving forward.
        </p>

      </div>
    </div>
  );
}
