import { useState } from "react";
import "./DonateCharity.css";


function RegisterVolunteer() {
  const [formData, setFormData] = useState({
    name: "",
    eventRegistered: "",
    age: "",
    gender: "",
    type: "",
    experience: "",
    email: "",
    contact: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Volunteer Registration Data:", formData);
  };

  return (
    <div className="register-container">
      <h2>Register as Volunteer</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="name" placeholder="Name" onChange={handleChange} required />
        <input type="text" name="eventRegistered" placeholder="Event Registered" onChange={handleChange} required />
        <input type="number" name="age" placeholder="Age" onChange={handleChange} required />
        <select name="gender" onChange={handleChange} required>
          <option value="">Select Gender</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
          <option value="Other">Other</option>
        </select>
        <select name="type" onChange={handleChange} required>
          <option value="">Type</option>
          <option value="Online">Online</option>
          <option value="Offline">Offline</option>
        </select>
        <input type="text" name="experience" placeholder="Experience" onChange={handleChange} />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="tel" name="contact" placeholder="Contact" onChange={handleChange} required />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default RegisterVolunteer;
