import { useState } from 'react';
import './RentalServiceDashboard.css';

const RentalServiceDashboard = () => {
  const [bikeData, setBikeData] = useState({
    photo: '',
    model: '',
    availability: '',
    times: '',
    additionalInfo: '',
    hourlyRate: '',
    pickUp: '',
    dropOff: ''
  });

  const handleChange = (e) => {
    setBikeData({ ...bikeData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Bike Rental Data:", bikeData);
  };

  return (
    <div className="rental-dashboard-container"> {/* Added wrapper div */}
      <div className="rental-dashboard">
        <h1>Rental Service Provider Dashboard</h1>
        <form onSubmit={handleSubmit}>
          <input type="file" name="photo" onChange={handleChange} />
          <input type="text" name="model" placeholder="Bike Model (optional)" onChange={handleChange} />
          <input type="text" name="availability" placeholder="Availability" onChange={handleChange} />
          <input type="text" name="times" placeholder="Times" onChange={handleChange} />
          <input type="text" name="hourlyRate" placeholder="Hourly Rate (₹)" onChange={handleChange} />
          <input type="text" name="pickUp" placeholder="Pick-Up Location (Jamshedpur)" onChange={handleChange} />
          <input type="text" name="dropOff" placeholder="Drop-Off Location (Jamshedpur)" onChange={handleChange} />
          <button type="submit">Submit</button>
        </form>
      </div>
    </div>
  );
};

export default RentalServiceDashboard;
