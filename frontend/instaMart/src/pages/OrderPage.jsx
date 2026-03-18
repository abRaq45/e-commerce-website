import React, { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";

const OrderPage = () => {

  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [pincode, setPincode] = useState("");

  const placeOrder = async () => {
    try {
      const res = await API.post(`/orders/${userId}`, {
        address,
        city,
        pincode
      });

      const order = res.data;

      // navigate to billing page
      navigate("/bill", { state: { order } });

    } catch (error) {
      console.error(error);
      alert("Order failed");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Order Details</h2>

      <input
        className="form-control mb-2"
        placeholder="Address"
        value={address}
        onChange={(e) => setAddress(e.target.value)}
      />

      <input
        className="form-control mb-2"
        placeholder="City"
        value={city}
        onChange={(e) => setCity(e.target.value)}
      />

      <input
        className="form-control mb-2"
        placeholder="Pincode"
        value={pincode}
        onChange={(e) => setPincode(e.target.value)}
      />

      <button className="btn btn-success btn-lg mt-3" onClick={placeOrder}>
        Confirm Order
      </button>
    </div>
  );
};

export default OrderPage;