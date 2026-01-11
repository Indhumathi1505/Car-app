import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/api";
import "../UsedCars/UsedCar.css";

export default function BrandCars() {
  const { brand } = useParams(); // honda / bmw
  const navigate = useNavigate();

  const [cars, setCars] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(`api/cars/brand/${brand}`)
      .then((res) => {
        setCars(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, [brand]);

  if (loading) return <p>Loading {brand} cars...</p>;

  return (
    <div className="used-cars-page">
      <h2 className="page-title">{brand.toUpperCase()} Cars</h2>

      <div className="car-grid">
        {cars.length === 0 && <p>No cars found</p>}

        {cars.map((car) => (
          <div className="car-card" key={car.id}>
            <div className="car-image-box">
              <img
                src={`data:image/jpeg;base64,${car.image}`}
                alt={car.title}
              />
            </div>

            <div className="car-details">
              <h3>{car.title}</h3>
              <p className="car-price">
                Rs. {Number(car.price).toLocaleString("en-IN")}
              </p>
              <p>{car.condition} Car</p>

              <button
                className="view-details-btn"
                onClick={() => navigate(`/car/${car.id}`)}
              >
                View Details
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
