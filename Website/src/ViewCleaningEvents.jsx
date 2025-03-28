import { Link } from "react-router-dom";
import "./ViewCleaningEvents.css";


function ViewCleaningEvents() {
  return (
    <div className="events-container">
      <h2>View Events</h2>

      <div className="event-section">
        <h3>Events for Hunger</h3>
        <div className="event-box">
          <p>Food Drive for the Homeless</p>
          <Link to="/register-volunteer">Register as Volunteer</Link>
          <Link to="/donate-charity">Donate for Charity</Link>
        </div>
        <div className="event-box">
          <p>Community Kitchen Initiative</p>
          <Link to="/register-volunteer">Register as Volunteer</Link>
          <Link to="/donate-charity">Donate for Charity</Link>
        </div>
      </div>

      <div className="event-section">
        <h3>Events for Sanitation</h3>
        <div className="event-box">
          <p>Public Toilet Maintenance Drive</p>
          <Link to="/register-volunteer">Register as Volunteer</Link>
          <Link to="/donate-charity">Donate for Charity</Link>
        </div>
      </div>

      <div className="event-section">
        <h3>Blood Donation Events</h3>
        <div className="event-box">
          <p>Annual Blood Donation Camp</p>
          <Link to="/register-volunteer">Register as Volunteer</Link>
          <Link to="/donate-charity">Donate for Charity</Link>
        </div>
      </div>

      <div className="event-section">
        <h3>Cleaning Events</h3>
        <div className="event-box">
          <p>Beach Cleanup Drive</p>
          <Link to="/register-volunteer">Register as Volunteer</Link>
          <Link to="/donate-charity">Donate for Charity</Link>
        </div>
      </div>
    </div>
  );
}

export default ViewCleaningEvents;
