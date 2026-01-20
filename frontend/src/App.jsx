import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";

import Home from "./Home/Home";
import Signup from "./pages/Signup";
import Login from "./pages/Login";
import AboutUs from "./pages/Aboutus";
import RateUs from "./Rate/Rateus";
import ProfileInfo from "./pages/info";
import Profile from "./pages/Profile";
import SellCar from "./SellCars/SellCarBase";
import SellRole from "./SellCars/SellRole";
import UsedCar from "./UsedCars/UsedCar";
import CarDetails from "./pages/UsedCarDetails";
import Favourites from "./pages/Favourites";

import ShowroomLogin from "./pages/ShowroomLogin";
import ShowroomSignup from "./pages/ShowroomSignup";

import UploadCar from "./pages/UploadCar";

import NewCars from "./pages/NewCars";
import SellerLogin from "./SellCars/SellerLogin";
import SellerSignin from "./SellCars/SellerSignin";
// Admin
import AdminLogin from "./Admin/AdminLogin";
import AdminPending from "./Admin/AdminPending";
import SellUsedCar from "./SellCars/SellUsedCar";
import SellNewCar from "./SellCars/SellNewCar";
import NewCarDetails from "./pages/NewCarDetails";
import SellerInbox from "./pages/SellerInbox";
import BrandCars from "./Home/BrandCars";

function App() {
  return (
    <Routes>
      {/* USER */}
     
      <Route path="/seller/inbox" element={<SellerInbox />} />
      <Route path="/brand/:brand" element={<BrandCars />} />

     


      <Route path="/sell/used" element={<SellUsedCar />} />
<Route path="/sell/new" element={<SellNewCar />} />

      <Route path="/" element={<Home />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/about-us" element={<AboutUs />} />
      <Route path="/rate-us" element={<RateUs />} />
      <Route path="/info" element={<ProfileInfo />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/used-cars" element={<UsedCar />} />
      <Route path="/new-cars" element={<NewCars />} />
      <Route path="/car/:id" element={<CarDetails />} />
       <Route path="/new-car/:id" element={<NewCarDetails />} />
      <Route path="/favourites" element={<Favourites />} />

      {/* SELLER / SHOWROOM */}
      <Route path="/sell-login" element={<SellerLogin />} />
      <Route path="/sell-signin" element={<SellerSignin />} />
      <Route path="/sell-role" element={<SellRole />} />
      <Route path="/sell-cars" element={<SellCar />} />
      <Route path="/showroom/login" element={<ShowroomLogin />} />
      <Route path="/showroom/signup" element={<ShowroomSignup />} />
     
      <Route path="/showroom/upload" element={<UploadCar />} />

      {/* ADMIN */}
      <Route path="/admin/login" element={<AdminLogin />} />
      <Route path="/admin/pending" element={<AdminPending />} />

      {/* FALLBACK */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default App;
