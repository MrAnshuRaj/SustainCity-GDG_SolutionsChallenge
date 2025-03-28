import { useState } from "react";
import "./LoginPage.css";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    city: "",
    userType: "Citizen",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Form Data Submitted:", formData);
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>LOGIN</h2>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="city"
          placeholder="City"
          value={formData.city}
          onChange={handleChange}
          required
        />
        <select name="userType" value={formData.userType} onChange={handleChange}>
          <option value="Citizen">Citizen</option>
          <option value="Volunteer">Volunteer</option>
        </select>
        <button type="submit" className="sign-up-btn">Sign Up</button>
        <p className="login-link">Already have an account? <a href="/login">Login</a></p>
      </form>
    </div>
  );
};

export default LoginPage;
