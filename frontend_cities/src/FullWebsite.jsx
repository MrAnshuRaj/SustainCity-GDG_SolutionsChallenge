// FullWebsite.jsx
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import './App.css';
import SustainCityLogo from './SustainCity..png';
import SignUpPage from './SignUpPage.jsx';

import LoginPage from './LoginPage.jsx';
import FileComplaint from './FileComplaint.jsx';
import ViewCleaningEvents from './ViewCleaningEvents.jsx';
import DonateExcessFood from './DonateExcessFood.jsx';
import SustainabilityBlogs from './SustainabilityBlogs.jsx';
import Logout from './Logout.jsx';
import RegisterVolunteer from './RegisterVolunteer.jsx';
import DonateCharity from './DonateCharity.jsx';
import NGODashboard from './NGODashboard.jsx';
import RentalServiceDashboard from './RentalServiceDashboard.jsx';
import BikeRentalServices from './BikeRentalServices.jsx';

function Home() {
  return (
    <div className="container">
      <div className="MainImage">
        <img src={SustainCityLogo} alt="SustainCity" />
      </div>

      <div className="actionsTitle">
        <h1>ACTIONS</h1>
      </div>

      <ul className="actions-list">
        <li><Link to="/website/file-complaint"><h3>File a Complaint</h3></Link></li>
        <li><Link to="/website/view-cleaning-events"><h3>View Events</h3></Link></li>
        <li><Link to="/website/donate-excess-food"><h3>Donate Excess Food</h3></Link></li>
        <li><Link to="/website/sustainability-blogs"><h3>Read Sustainability Blogs</h3></Link></li>
        <li><Link to="/website/bike-rental-services"><h3>Bike rental service</h3></Link></li>
      </ul>
    </div>
  );
}

function Navbar() {
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);

  const handleLoginSelection = (path) => {
    navigate(path);
    setShowDropdown(false);
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <ul className="nav-links">
          <li><Link to="/website">Home</Link></li>
          <li className="dropdown">
            <button onClick={() => setShowDropdown(!showDropdown)} className="dropdown-btn">
              Login <span className="dropdown-arrow">▼</span>
            </button>
            {showDropdown && (
              <ul className="dropdown-menu">
                <li onClick={() => handleLoginSelection('/website/login')}>Login as Volunteer/Citizen</li>
                <li onClick={() => handleLoginSelection('/website/ngo-dashboard')}>Login as NGO</li>
                <li onClick={() => handleLoginSelection('/website/rental-dashboard')}>Login as Rental Service Provider</li>
              </ul>
            )}
          </li>
          <li><Link to="/website/logout">Logout</Link></li>
        </ul>
      </div>
    </nav>
  );
}

function FullWebsite() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/file-complaint" element={<FileComplaint />} />
        <Route path="/view-cleaning-events" element={<ViewCleaningEvents />} />
        <Route path="/donate-excess-food" element={<DonateExcessFood />} />
        <Route path="/sustainability-blogs" element={<SustainabilityBlogs />} />
        <Route path="/bike-rental-services" element={<BikeRentalServices />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/register-volunteer" element={<RegisterVolunteer />} />
        <Route path="/donate-charity" element={<DonateCharity />} />
        <Route path="/ngo-dashboard" element={<NGODashboard />} />
        <Route path="/rental-dashboard" element={<RentalServiceDashboard />} />
        <Route path="/sign-up" element={<SignUpPage />} />

      </Routes>
    </>
  );
}

export default FullWebsite;
