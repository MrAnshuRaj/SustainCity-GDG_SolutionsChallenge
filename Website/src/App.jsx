import { BrowserRouter as Router, Route, Routes, Link, useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './App.css';
import SustainCityLogo from './SustainCity..png';

// Import all pages
import IntroductoryPage from './IntroductoryPage.jsx';
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
      <div className='MainImage'><img src={SustainCityLogo} alt="SustainCity" /></div>
      <br /><br /><br />
      <div className='actionsTitle'><h1>ACTIONS</h1></div>

      <ul className='actions-list'>
        <li><Link to="/file-complaint"><h3>File a Complaint</h3></Link></li>
        <li><Link to="/view-cleaning-events"><h3>View Events</h3></Link></li>
        <li><Link to="/donate-excess-food"><h3>Donate Excess Food</h3></Link></li>
        <li><Link to="/sustainability-blogs"><h3>Read Sustainability Blogs</h3></Link></li>
        <li><Link to="/bike-rental-services"><h3>Bike rental service</h3></Link></li> 
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
    <nav className='navbar'>
      <div className='navbar-container'>
        <ul className='nav-links'>
          <li><Link to="/">Home</Link></li>
          <li className="dropdown">
            <button onClick={() => setShowDropdown(!showDropdown)} className="dropdown-btn">
              Login <span className="dropdown-arrow">▼</span>
            </button>
            {showDropdown && (
              <ul className="dropdown-menu">
                <li onClick={() => handleLoginSelection('/login')}>Login as Volunteer/Citizen</li>
                <li onClick={() => handleLoginSelection('/ngo-dashboard')}>Login as NGO</li>
                <li onClick={() => handleLoginSelection('/rental-dashboard')}>Login as Rental Service Provider</li>
              </ul>
            )}
          </li>
          <li><Link to="/logout">Logout</Link></li>
        </ul>
      </div>
    </nav>
  );
}

function ScrollToTop({ children })
{
  const location = useLocation();

  useEffect(()=>{
    window.scrollTo(0,0);
  },[location]);
  return <>{children}</>
}

function App() {
  return (
    <Router>
    <ScrollToTop>
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
      </Routes>
    </ScrollToTop>
    </Router>
  );
}

export default App;
