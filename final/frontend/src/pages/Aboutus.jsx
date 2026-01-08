import "./AboutUs.css";
import carImg from "../assets/car.jpg";

/* ✅ Import Navbar */
import Navbar from "../Home/Navbar";

export default function AboutUs() {
  return (
    <>
      {/* ✅ Navbar */}
      <Navbar />

      <div className="about-container">
        <div className="about-card">

          {/* TITLE */}
          <h1 className="about-title">ABOUT US</h1>

          {/* WHO WE ARE */}
          <h3 className="about-subtitle">Who we are</h3>
          <p className="about-text">
            Cartrizo is a modern and reliable digital car marketplace that connects
            buyers with certified sellers across the country. We focus on trust,
            transparency, and convenience to help users discover high-quality new
            and used vehicles effortlessly.
          </p>

          {/* WHAT WE DO */}
          <h3 className="about-subtitle">What we do</h3>
          <p className="about-text">
            We enable users to browse detailed car listings, compare models, check
            real-time pricing, and communicate securely with verified sellers.
            Our intelligent search and filtering system ensures you find the
            perfect vehicle that matches your budget and preferences.
          </p>

          {/* EXTRA CONTENT */}
          <div className="about-extra">
            <div className="extra-box">
              <h4>Our Mission</h4>
              <p>
                To simplify the car buying and selling process through a secure,
                transparent, and user-friendly platform powered by technology.
              </p>
            </div>

            <div className="extra-box">
              <h4>Our Vision</h4>
              <p>
                To become India’s most trusted automobile marketplace, delivering
                exceptional value to buyers and sellers alike.
              </p>
            </div>
          </div>

          {/* WHY CHOOSE US */}
          <div className="about-middle">
            <div className="about-left">
              <h3 className="about-subtitle">Why Choose Us</h3>
              <ul className="about-list">
                <li>Verified car listings with quality checks</li>
                <li>Transparent pricing without hidden charges</li>
                <li>Secure communication between buyers and sellers</li>
                <li>Fast, simple, and easy-to-use interface</li>
                <li>Support for users of all age groups</li>
              </ul>
            </div>

            <div className="about-right">
              <img src={carImg} alt="Car" />
            </div>
          </div>

        </div>
      </div>
    </>
  );
}
