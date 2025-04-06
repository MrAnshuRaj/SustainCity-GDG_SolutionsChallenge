import React from 'react';
import './BikeRentalServices.css';

const bikeData = [
  {
    id: 1,
    image: 'https://images.pexels.com/photos/276517/pexels-photo-276517.jpeg', // Replace with actual bike image URL
    model: 'Hero Mountain Bike X200',
    availability: 'Available',
    times: '6 AM - 10 PM',
    additionalInfo: 'Great for off-road and hilly terrain.',
    hourlyRate: '₹150/hr',
    pickupLocation: 'Bistupur Market, Jamshedpur',
    dropLocation: 'Sakchi Main Road, Jamshedpur',
  },
  {
    id: 2,
    image: 'https://images.pexels.com/photos/100582/pexels-photo-100582.jpeg', // Replace with actual bike image URL
    model: 'Atlas City Bicycle Y500',
    availability: 'Available',
    times: '5 AM - 11 PM',
    additionalInfo: 'Best for comfortable city rides.',
    hourlyRate: '₹80/hr',
    pickupLocation: 'Sakchi Golchakkar, Jamshedpur',
    dropLocation: 'Tatanagar Railway Station, Jamshedpur',
  },
  {
    id: 3,
    image: 'https://images.pexels.com/photos/417255/pexels-photo-417255.jpeg', // Replace with actual bike image URL
    model: 'Lectro Electric Bike Z900',
    availability: 'Limited',
    times: '7 AM - 9 PM',
    additionalInfo: 'Battery-assisted pedaling with 40km range.',
    hourlyRate: '₹250/hr',
    pickupLocation: 'Kadma Market, Jamshedpur',
    dropLocation: 'Sonari Airport, Jamshedpur',
  },
];

const BikeRentalServices = () => {
  return (
    <div className="bike-rental-container">
      <h1>Bike Rental Services - Jamshedpur</h1>
      <div className="bike-list">
        {bikeData.map((bike) => (
          <div key={bike.id} className="bike-card">
            <img src={bike.image} alt={bike.model || 'Bicycle'} className="bike-image" />
            <div className="bike-content">
              <h2>{bike.model || 'Bicycle'}</h2>
              <p><strong>Availability:</strong> {bike.availability}</p>
              <p><strong>Times:</strong> {bike.times}</p>
              {bike.additionalInfo && <p><strong>Info:</strong> {bike.additionalInfo}</p>}
              <p><strong>Hourly Rate:</strong> {bike.hourlyRate}</p>
              <p><strong>Pick-up Location:</strong> {bike.pickupLocation}</p>
              <p><strong>Drop Location:</strong> {bike.dropLocation}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BikeRentalServices;
