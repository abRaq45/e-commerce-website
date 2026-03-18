import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axios";

const SignupPage = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      await API.post("/users/signup", {
        username: formData.username,
        password: formData.password,
      });

      alert("Signup successful!");
      navigate("/login");

    } catch (err) {
      console.error(err);
      setError(
        err.response?.data?.message ||
        err.response?.data ||
        "Signup failed"
      );
    }

    setLoading(false);
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center"
      style={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #141E30, #243B55)",
      }}
    >
      <form
        onSubmit={handleSubmit}
        className="p-4 rounded shadow-lg"
        style={{
          width: "400px",
          backgroundColor: "#1c1c1c",
          color: "white",
        }}
      >
        <h2 className="text-center mb-4 text-success">Create Account</h2>

        {error && <div className="alert alert-danger py-2">{error}</div>}

        <div className="mb-3">
          <label className="form-label">Username</label>
          <input
            type="text"
            name="username"
            className="form-control"
            value={formData.username}
            onChange={handleChange}
            required
            style={{ backgroundColor: "#2c2c2c", color: "white" }}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            type="password"
            name="password"
            className="form-control"
            value={formData.password}
            onChange={handleChange}
            required
            style={{ backgroundColor: "#2c2c2c", color: "white" }}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Confirm Password</label>
          <input
            type="password"
            name="confirmPassword"
            className="form-control"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
            style={{ backgroundColor: "#2c2c2c", color: "white" }}
          />
        </div>

        <button
          type="submit"
          className="btn btn-success w-100"
          disabled={loading}
        >
          {loading ? "Signing up..." : "Sign Up"}
        </button>

        <p className="mt-3 text-center">
          Already have an account?{" "}
          <span
            className="text-info"
            style={{ cursor: "pointer" }}
            onClick={() => navigate("/login")}
          >
            Login
          </span>
        </p>
      </form>
    </div>
  );
};

export default SignupPage;