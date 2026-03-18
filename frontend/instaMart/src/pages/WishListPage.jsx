import React, { useEffect, useState } from "react";
import API from "../api/axios";

const WishListPage = ({ user }) => {
  const [wishlistItems, setWishlistItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchWishlist = async () => {
      if (!user?.userId) return;

      try {
        // Step 1: Get wishlist product IDs
        const res = await API.get(`/users/${user.userId}/wishlist`);
        const productIds = res.data || [];

        // Step 2: Fetch full product details
        const products = await Promise.all(
          productIds.map((id) =>
            API.get(`/items/${id}`).then((res) => res.data)
          )
        );

        setWishlistItems(products);
      } catch (err) {
        console.error("Wishlist fetch failed:", err);
      }
      setLoading(false);
    };

    fetchWishlist();
  }, [user]);

  const removeWishlist = async (productId) => {
    try {
      await API.delete(`/users/${user.userId}/wishlist/${productId}`);

      setWishlistItems((prev) =>
        prev.filter((item) => item.id !== productId)
      );
    } catch (err) {
      console.error("Remove wishlist failed:", err);
    }
  };

  if (loading) return <h3 className="text-center mt-5">Loading...</h3>;

  return (
    <div className="container mt-4">
      <h2 className="mb-4">❤️ My Wishlist</h2>

      {wishlistItems.length === 0 ? (
        <p>Your wishlist is empty.</p>
      ) : (
        wishlistItems.map((item) => (
          <div
            key={item.id}
            className="card mb-3 p-3 d-flex flex-row justify-content-between align-items-center"
          >
            <div>
              <h5>{item.item}</h5>
              <p>₹ {item.price}</p>
            </div>

            <button
              className="btn btn-outline-danger"
              onClick={() => removeWishlist(item.id)}
            >
              Remove ❤️
            </button>
          </div>
        ))
      )}
    </div>
  );
};

export default WishListPage;