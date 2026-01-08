import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaTrashAlt } from "react-icons/fa";
import "./Favourites.css";

export default function Favourites() {
  const navigate = useNavigate();
  const [favourites, setFavourites] = useState([]);

  useEffect(() => {
    const favs = JSON.parse(localStorage.getItem("favourites")) || [];
    setFavourites(favs);
  }, []);

  const handleCarClick = (id) => {
    navigate(`/car/${id}`);
  };

  const handleRemove = (id) => {
    const updated = favourites.filter((car) => car.id !== id);
    setFavourites(updated);
    localStorage.setItem("favourites", JSON.stringify(updated));
  };

  if (favourites.length === 0) {
    return <div className="empty-msg">No favourite cars found!</div>;
  }

  return (
    <div className="favourites-page">
      <h1 className="page-title">My Favourites</h1>

      <div className="favourites-list">
        {favourites.map((car) => (
          <div
            key={car.id}
            className="car-card"
            onClick={() => handleCarClick(car.id)}
          >
            <img
              src={
                car.image
                  ? `data:image/jpeg;base64,${car.image}`
                  : "/placeholder-car.jpg"
              }
              alt={car.title}
            />

            <div className="car-card-content">
              <h3>{car.title}</h3>
              <p>₹{Number(car.price).toLocaleString()}</p>
            </div>

            <button
              className="remove-btn"
              onClick={(e) => {
                e.stopPropagation();
                handleRemove(car.id);
              }}
            >
              <FaTrashAlt /> Remove
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
