import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { FaHeart, FaRegHeart, FaPhoneAlt } from "react-icons/fa";
import "./CarDetails.css";

// ⭐ Chart imports
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

export default function NewCarDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  // ===== BASIC STATES =====
  const [car, setCar] = useState(null);
  const [image, setImage] = useState("");
  const [loading, setLoading] = useState(true);
  const [isFavourite, setIsFavourite] = useState(false);

  // ===== SHOWROOM CONTACT =====
  const [showroom, setShowroom] = useState(null);
  const [showModal, setShowModal] = useState(false);

  // ===== USER =====
  const loggedUser = JSON.parse(localStorage.getItem("user"));
  const buyerEmail = loggedUser?.email;

  // ===== REVIEWS =====
  const [reviews, setReviews] = useState([]);
  const [newReview, setNewReview] = useState({ rating: 5, comment: "" });

  // ===== CONTACT DEALER =====
  const contactDealer = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8080/api/cars/contact/${id}`
      );
      setShowroom(res.data);
      setShowModal(true);
    } catch (err) {
      alert("Failed to get showroom info");
      console.error(err);
    }
  };

  // ===== FETCH CAR =====
  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/cars/${id}`)
      .then(res => {
        setCar(res.data);
        setImage(res.data.image);
      })
      .catch(() => setCar(null))
      .finally(() => setLoading(false));
  }, [id]);

  // ===== FETCH REVIEWS =====
  const fetchReviews = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8080/api/reviews/car/${id}`
      );
      setReviews(res.data);
    } catch (err) {
      console.error("Failed to fetch reviews", err);
      setReviews([]);
    }
  };

  useEffect(() => {
    fetchReviews();
  }, [id]);

  // ===== FAVOURITES =====
  useEffect(() => {
    const favs = JSON.parse(localStorage.getItem("newFavourites")) || [];
    setIsFavourite(favs.some(c => c.id === id));
  }, [id]);

  const toggleFavourite = () => {
    const favs = JSON.parse(localStorage.getItem("newFavourites")) || [];
    if (!car) return;

    if (isFavourite) {
      localStorage.setItem(
        "newFavourites",
        JSON.stringify(favs.filter(c => c.id !== car.id))
      );
      setIsFavourite(false);
    } else {
      favs.push(car);
      localStorage.setItem("newFavourites", JSON.stringify(favs));
      setIsFavourite(true);
    }
  };

  // ===== SUBMIT REVIEW =====
  const submitReview = async (e) => {
    e.preventDefault();

    if (!buyerEmail) {
      alert("Please login to submit a review");
      navigate("/login");
      return;
    }

    const payload = {
      carId: id,
      userEmail: buyerEmail,
      rating: Number(newReview.rating),
      comment: newReview.comment
    };

    try {
      await axios.post("http://localhost:8080/api/reviews", payload);
      setNewReview({ rating: 5, comment: "" });
      fetchReviews();
      alert("Review submitted successfully!");
    } catch (err) {
      console.error(err);
      alert("Failed to submit review");
    }
  };

  // ===== PIE CHART DATA =====
  const counts = [0, 0, 0, 0, 0];
  reviews.forEach(r => counts[r.rating - 1]++);

  const pieData = {
    labels: ["1★", "2★", "3★", "4★", "5★"],
    datasets: [
      {
        data: counts,
        backgroundColor: [
          "#ce8f9cff",
          "#73c3f8ff",
          "#eed493ff",
          "#9be8e8ff",
          "#b69de8ff"
        ]
      }
    ]
  };

  // ===== LOADING =====
  if (loading) return <div className="loading">Loading...</div>;
  if (!car) return <div className="loading">Car not found</div>;

  return (
    <div className="cardetails-page">

      <button className="back-btn" onClick={() => navigate("/new-cars")}>
        ← Back to New Cars
      </button>

      {/* IMAGE */}
      <div className="image-section">
        <img
          className="main-image"
          src={`data:image/jpeg;base64,${image}`}
          alt="car"
        />

        <button className="favourite-btn" onClick={toggleFavourite}>
          {isFavourite ? <FaHeart /> : <FaRegHeart />}
        </button>
      </div>

      {/* DETAILS */}
      <div className="details-section">
        <h1>{car.title}</h1>
        <div className="price">₹{Number(car.price).toLocaleString()}</div>

        <table className="spec-table">
          <tbody>
            <tr><th>Brand</th><td>{car.brand}</td></tr>
            <tr><th>Model</th><td>{car.model}</td></tr>
            <tr><th>Year</th><td>{car.year}</td></tr>
            <tr><th>Fuel</th><td>{car.fuelType}</td></tr>
            <tr><th>Mileage</th><td>{car.mileage} km</td></tr>
            <tr><th>Condition</th><td>New</td></tr>
          </tbody>
        </table>

        {/* DESCRIPTION */}
        <div className="description">
          <h3>Description</h3>
          <p>{car.description}</p>
        </div>

        {/* FEATURES */}
        <div className="features">
          <h3>Features</h3>
          <div className="feature-list">
            {car.features?.length ? (
              car.features.map((f, i) => (
                <span key={i} className="feature-item">{f}</span>
              ))
            ) : (
              <p>No features listed</p>
            )}
          </div>
        </div>

        {/* CONTACT */}
        <div className="action-buttons">
          <button className="contact-btn" onClick={contactDealer}>
            <FaPhoneAlt /> Contact Dealer
          </button>
        </div>
      </div>

      {/* SHOWROOM MODAL */}
      {showModal && showroom && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>{showroom.name}</h2>
            <p>Email: {showroom.email}</p>
            <p>Phone: {showroom.phone}</p>
            <p>Address: {showroom.address}</p>
            <button onClick={() => setShowModal(false)}>Close</button>
          </div>
        </div>
      )}

      {/* REVIEWS */}
      <div className="reviews-section">
        <h2>Customer Reviews</h2>

        {reviews.length > 0 ? (
          <div className="pie-chart">
            <Pie data={pieData} />
          </div>
        ) : (
          <p>No reviews yet</p>
        )}

        {/* ADD REVIEW */}
        <div className="review-form">
          <h3>Add Your Review</h3>

          <form onSubmit={submitReview}>
            <div className="star-rating">
              {[1, 2, 3, 4, 5].map(star => (
                <span
                  key={star}
                  className={`star ${newReview.rating >= star ? "active" : ""}`}
                  onClick={() =>
                    setNewReview({ ...newReview, rating: star })
                  }
                >
                  ★
                </span>
              ))}
            </div>

            <textarea
              placeholder="Write your review"
              value={newReview.comment}
              onChange={e =>
                setNewReview({ ...newReview, comment: e.target.value })
              }
              required
            />

            <button type="submit">Submit Review</button>
          </form>
        </div>

        {/* REVIEW LIST */}
        <div className="review-list">
          {reviews.length ? (
            reviews.map(r => (
              <div key={r._id||r.id} className="review-card">
                <strong>{r.userEmail}</strong>
                <div className="review-stars">
                  {"★".repeat(r.rating)}
                </div>
                <p>{r.comment}</p>
              </div>
            ))
          ) : (
            <p>No reviews yet</p>
          )}
        </div>
      </div>

    </div>
  );
}
