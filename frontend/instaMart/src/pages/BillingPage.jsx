import React from "react";
import { useLocation } from "react-router-dom";

const BillingPage = () => {

  const location = useLocation();
  const order = location.state?.order;

  if (!order) {
    return <h3 className="text-center mt-5">No order found</h3>;
  }

  return (
    <div className="container mt-4">

      <div className="card p-4">

        <h2 className="text-center text-success">
          🎉 Thanks for ordering!
        </h2>

        <hr />

        <h5>Delivery Address</h5>
        <p>
          {order.address}, {order.city} - {order.pincode}
        </p>

        <hr />

        <h5>Order Items</h5>

        {order.items.map((item) => (
          <div
            key={item.productId}
            className="d-flex justify-content-between"
          >
            <span>
              {item.productName} × {item.quantity}
            </span>

            <span>
              ₹ {item.price * item.quantity}
            </span>
          </div>
        ))}

        <hr />

        <div className="d-flex justify-content-between">
          <strong>Subtotal</strong>
          <span>₹ {order.subtotal}</span>
        </div>

        <div className="d-flex justify-content-between">
          <strong>Tax (18%)</strong>
          <span>₹ {order.tax}</span>
        </div>

        <div className="d-flex justify-content-between">
          <strong>Delivery Fee</strong>
          <span>₹ {order.deliveryFee}</span>
        </div>

        <hr />

        <div className="d-flex justify-content-between">
          <h4>Total</h4>
          <h4>₹ {order.totalAmount}</h4>
        </div>

      </div>
    </div>
  );
};

export default BillingPage;