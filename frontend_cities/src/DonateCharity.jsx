import { useState } from "react";
import "./DonateCharity.css";


function DonateCharity() {
  const [formData, setFormData] = useState({
    name: "",
    age: "",
    contact: "",
    email: "",
    amount: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Charity Donation Data:", formData);
  };

  return (
    <div className="donate-container">
      <h2>Donate for Charity</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="name" placeholder="Name" onChange={handleChange} required />
        <input type="number" name="age" placeholder="Age" onChange={handleChange} required />
        <input type="tel" name="contact" placeholder="Contact" onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="number" name="amount" placeholder="Amount to be Donated" onChange={handleChange} required />
        <button type="submit">Donate</button>
      </form>
    </div>
  );
}

export default DonateCharity;
