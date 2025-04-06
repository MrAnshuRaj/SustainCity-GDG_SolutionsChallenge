import { useState } from 'react';
import './NGODashboard.css';

const NGODashboard = () => {
  const [food, setFood] = useState([]);
  const [events, setEvents] = useState([]);
  const [foodData, setFoodData] = useState({ foodName: '', quantity: '', expiry: '' });
  const [eventData, setEventData] = useState({ name: '', date: '', location: '' });

  const addFood = () => {
    setFood([...food, foodData]);
    setFoodData({ foodName: '', quantity: '', expiry: '' });
  };

  const createEvent = () => {
    setEvents([...events, eventData]);
    setEventData({ name: '', date: '', location: '' });
  };

  return (
    <div className="ngo-dashboard-container">
      <div className="ngo-dashboard">
        <h1>NGO Dashboard</h1>

        <div className="section">
          <h2>Add Food for Distribution</h2>
          <input type="text" placeholder="Food Name" value={foodData.foodName} onChange={(e) => setFoodData({ ...foodData, foodName: e.target.value })} />
          <input type="number" placeholder="Quantity" value={foodData.quantity} onChange={(e) => setFoodData({ ...foodData, quantity: e.target.value })} />
          <input type="date" value={foodData.expiry} onChange={(e) => setFoodData({ ...foodData, expiry: e.target.value })} />
          <button onClick={addFood}>Add Food</button>
        </div>

        <div className="section">
          <h2>Manage Cleaning Events</h2>
          <input type="text" placeholder="Event Name" value={eventData.name} onChange={(e) => setEventData({ ...eventData, name: e.target.value })} />
          <input type="date" value={eventData.date} onChange={(e) => setEventData({ ...eventData, date: e.target.value })} />
          <input type="text" placeholder="Location" value={eventData.location} onChange={(e) => setEventData({ ...eventData, location: e.target.value })} />
          <button onClick={createEvent}>Create Event</button>
        </div>

        <div className="section">
          <h2>Donated Food Data</h2>
          {food.length > 0 ? food.map((item, index) => (
            <p key={index}>{item.foodName} - {item.quantity} units, expires on {item.expiry}</p>
          )) : <p>No food donations yet.</p>}
        </div>
      </div>
    </div>
  );
};

export default NGODashboard;
