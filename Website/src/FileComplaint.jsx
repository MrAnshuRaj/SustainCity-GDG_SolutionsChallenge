import React, { useState } from "react";
import "./FileComplaint.css";

const FileComplaint = () => {
  const [complaintTitle, setComplaintTitle] = useState("");
  const [description, setDescription] = useState("");
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
      complaintTitle,
      description,
      location,
      image,
    });
    alert("Complaint Submitted Successfully!");
  };

  return (
    <div className="file-complaint-container">
      <h2>File a Complaint</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Complaint Title"
          value={complaintTitle}
          onChange={(e) => setComplaintTitle(e.target.value)}
          required
        />
        <textarea
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Location"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          required
        />
        <input type="file" accept="image/*" onChange={handleImageUpload} />
        {image && <img src={image} alt="Uploaded Preview" className="image-preview" />}
        <button type="submit">Submit Complaint</button>
      </form>
    </div>
  );
};

export default FileComplaint;
