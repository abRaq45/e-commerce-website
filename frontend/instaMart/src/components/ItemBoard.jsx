import React from "react";
import ItemCard from "./ItemCard";

const ItemBoard = ({ items = [], user }) => {
  if (!items.length) {
    return (
      <p className="text-light text-center mt-5">
        No products available.
      </p>
    );
  }

  return (
    <div className="container my-4">
      <div className="row g-4">
        {items.map((item) => (
          <div key={item.id} className="col-md-4 col-lg-3">
            <ItemCard item={item} user={user} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ItemBoard;