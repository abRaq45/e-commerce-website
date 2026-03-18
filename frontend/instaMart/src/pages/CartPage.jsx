import React, { useEffect, useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";

const CartPage = ({ user }) => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const userId = user?.userId || localStorage.getItem("userId");

  useEffect(() => {
    const fetchCart = async () => {
      if (!userId) return;

      try {
        const res = await API.get(`/cart/${userId}`);
        setCartItems(res.data || []);
      } catch (err) {
        console.error("Cart fetch failed:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchCart();
  }, [userId]);

  const removeItem = async (productId) => {
    try {
      await API.delete(`/cart/${userId}/remove/${productId}`);

      setCartItems((prev) =>
        prev.filter((item) => item.productId !== productId)
      );
    } catch (err) {
      console.error("Remove failed:", err);
    }
  };

  const totalPrice = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (loading) {
    return <h3 className="text-center mt-5">Loading...</h3>;
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-4">🛒 My Cart</h2>

      {cartItems.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <>
          {cartItems.map((item) => (
            <div
              key={item.productId}
              className="card mb-3 p-3 d-flex flex-row justify-content-between align-items-center"
            >
              <div>
                <h5>{item.productName}</h5>
                <p>
                  ₹ {item.price} × {item.quantity}
                </p>
              </div>

              <button
                className="btn btn-danger"
                onClick={() => removeItem(item.productId)}
              >
                Remove
              </button>
            </div>
          ))}

          <div className="text-end mt-4">
            <h4>Total: ₹ {totalPrice}</h4>

           <button
              className="btn btn-success btn-lg mt-3 px-5 py-3"
              onClick={() => navigate("/order")}
              >
               🛍 Place Order
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default CartPage;