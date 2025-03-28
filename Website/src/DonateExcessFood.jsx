import React, { useState } from "react";
import "./DonateExcessFood.css";

const DonateExcessFood = () => {
  const [foodName, setFoodName] = useState("");
  const [quantity, setQuantity] = useState("");
  const [bestBefore, setBestBefore] = useState("");
  const [location, setLocation] = useState("");
  const [image, setImage] = useState(null);

  const handleImageUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      setImage(URL.createObjectURL(file));
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log({
      foodName,
      quantity,
      bestBefore,
      location,
      image,
    });
    alert("Donation Submitted Successfully!");
  };

  return (
    <div className="donate-food-container">
      <h2>DONATE FOOD</h2>
      <form onSubmit={handleSubmit}>
        <label>Food Name</label>
        <input
          type="text"
          placeholder="Enter Food Name"
          value={foodName}
          onChange={(e) => setFoodName(e.target.value)}
          required
        />

        <label>Quantity</label>
        <input
          type="text"
          placeholder="e.g. 5 meals"
          value={quantity}
          onChange={(e) => setQuantity(e.target.value)}
          required
        />

        <label>Best Before</label>
        <input
          type="date"
          value={bestBefore}
          onChange={(e) => setBestBefore(e.target.value)}
          required
        />

        <label>Location</label>
        <input
          type="text"
          placeholder="Enter Location"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          required
        />

        <label>Upload Food Image</label>
        <input type="file" accept="image/*" onChange={handleImageUpload} />
        {image && <img src={image} alt="Uploaded Preview" className="image-preview" />}

        <button type="submit">Submit Donation</button>
      </form>
    </div>
  );
};

export default DonateExcessFood;
