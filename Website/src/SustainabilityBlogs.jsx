import React, { useState } from "react";
import "./blog_page_styles.css";

const ReadSustainabilityBlogs = () => {
  const [blogs, setBlogs] = useState([
    {
      title: "10 Ways to Reduce Plastic Waste",
      content: "Reducing plastic waste is easier than you think...",
      images: ["/images/plastic1.jpg"],
    },
    {
      title: "Sustainable Living Tips",
      content: "Sustainable living is about making eco-friendly choices...",
      images: ["/images/sustainable1.jpg"],
    },
  ]);

  const [newBlog, setNewBlog] = useState({ title: "", content: "", images: [] });

  const handleInputChange = (e) => {
    setNewBlog({ ...newBlog, [e.target.name]: e.target.value });
  };

  const handleImageUpload = (e) => {
    const files = Array.from(e.target.files).slice(0, 3);
    const imageUrls = files.map((file) => URL.createObjectURL(file));
    setNewBlog((prev) => ({ ...prev, images: imageUrls }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (newBlog.title.trim() && newBlog.content.trim()) {
      setBlogs([newBlog, ...blogs]); // Append new blog at the start
      setNewBlog({ title: "", content: "", images: [] });
    }
  };

  return (
    <div className="blog-container">
      <h2>Read Sustainability Blogs</h2>
      <p>Explore and learn about various sustainability topics.</p>

      {/* Display Blogs */}
      <div className="blog-list">
        {blogs.length > 0 ? (
          blogs.map((blog, index) => (
            <div key={index} className="blog-item">
              <h3>{blog.title}</h3>
              <p>{blog.content}</p>
              <div className="image-gallery">
                {blog.images.map((img, imgIndex) => (
                  <img key={imgIndex} src={img} alt="blog" className="blog-image" />
                ))}
              </div>
            </div>
          ))
        ) : (
          <p>No blogs available.</p>
        )}
      </div>

      {/* Upload Blog Section */}
      <div className="blog-upload">
        <h3>Upload Your Blog</h3>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="title"
            placeholder="Blog Title"
            value={newBlog.title}
            onChange={handleInputChange}
            required
          />
          <textarea
            name="content"
            placeholder="Write your blog (max 500 words)"
            value={newBlog.content}
            onChange={handleInputChange}
            maxLength={500}
            required
          />
          <input type="file" multiple accept="image/*" onChange={handleImageUpload} />
          <button type="submit">Upload Blog</button>
        </form>
      </div>
    </div>
  );
};

export default ReadSustainabilityBlogs;
