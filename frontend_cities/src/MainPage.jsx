// MainPage.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import SustainCityLogo from './SustainCity..png';
import './MainPage.css';
function MainPage() {
  return (
    <div className="main-page-container">
      <img src={SustainCityLogo} alt="SustainCity" className="main-logo" />

      <div className="main-options">
      <Link to="/download-app" className="main-box">Download the App!</Link>
        <Link to="/website" className="main-box">Visit Website!</Link>
       


      </div>
    </div>
  );
}

export default MainPage;
