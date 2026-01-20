import { useNavigate } from "react-router-dom";
import Navbar from "../Home/Navbar";
import "./SellRole.css";

export default function SellRole() {
  const navigate = useNavigate();

  return (
    <>
      <Navbar />
      <div className="role-select-container">
        <h2>What are you selling?</h2>

        <div className="role-cards">
          <div
            className="role-card"
            onClick={() => navigate("/showroom/login")}
          >
            <h3>🚗 New Car</h3>
            <p>Sell brand new cars via showroom</p>
          </div>

          <div
            className="role-card"
            onClick={() => navigate("/sell-signin")}
          >
            <h3>🚙 Used Car</h3>
            <p>Sell your used car</p>
          </div>
        </div>
      </div>
    </>
  );
}
