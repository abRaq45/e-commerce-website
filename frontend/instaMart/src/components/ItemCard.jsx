import React, { useState } from "react";
import API from "../api/axios";

const ItemCard = ({ item, user }) => {
  const userId = localStorage.getItem("userId");

  const [inCart, setInCart] = useState(false);
  const [inWishlist, setInWishlist] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleCartToggle = async () => {
    if (!user) return;

    setLoading(true);
    try {
      if (inCart) {
        await API.delete(`/cart/${userId}/remove/${item.id}`);
        setInCart(false);
      } else {
        await API.post(
          `/cart/${userId}/add/${item.id}?quantity=1`
        );
        setInCart(true);
      }
    } catch (err) {
      console.error("Cart update failed:", err);
    }
    setLoading(false);
  };

  const handleWishlistToggle = async () => {
    if (!user) return;

    setLoading(true);
    try {
      if (inWishlist) {
        await API.delete(`/users/${userId}/wishlist/${item.id}`);
        setInWishlist(false);
      } else {
        await API.post(`/users/${userId}/wishlist/${item.id}`);
        setInWishlist(true);
      }
    } catch (err) {
      console.error("Wishlist update failed:", err);
    }
    setLoading(false);
  };

  return (
    <div className="product-card card shadow-sm h-100 border-0 position-relative">

      {user && (
        <button
          className={`wishlist-heart ${inWishlist ? "active" : ""}`}
          onClick={handleWishlistToggle}
          disabled={loading}
        >
          {inWishlist ? "❤️" : "🤍"}
        </button>
      )}

      <div className="product-image-wrapper">
        <img
          src={
            item.itemPosterUrl && item.itemPosterUrl.trim() !== ""
              ? item.itemPosterUrl
              : "https://via.placeholder.com/400x400"
          }
          alt={item.item}
          className="product-image"
        />
      </div>

      <div className="card-body d-flex flex-column">
        <h5 className="fw-bold mb-1">{item.item}</h5>

        <p className="text-muted small mb-2">
          {item.category}
        </p>

        <h6 className="fw-bold text-success mb-3">
          ₹ {item.price}
        </h6>

        {user && (
          <div className="mt-auto">
            <button
              className={`btn ${
                inCart ? "btn-outline-danger" : "btn-success"
              } w-100`}
              onClick={handleCartToggle}
              disabled={loading}
            >
              {inCart ? "Remove from Cart" : "Add to Cart"}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ItemCard;