import React, { useEffect, useState } from "react";
import API from "../api/axios";
import ItemBoard from "../components/ItemBoard";

const HomePage = ({ user, filters }) => {
  const [items, setItems] = useState([]);

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const res = await API.get("/items");
        setItems(res.data);
      } catch (err) {
        console.error("Failed to fetch items:", err);
      }
    };

    fetchItems();
  }, []);

  // 🔥 Apply Multi Filtering + Sorting
  let filteredItems = [...items];

  // 🔍 SEARCH
  if (filters?.search) {
    filteredItems = filteredItems.filter(item =>
      item.item.toLowerCase().includes(filters.search.toLowerCase())
    );
  }

  // 📂 CATEGORY
  if (filters?.category && filters.category !== "All") {
    filteredItems = filteredItems.filter(
      item => item.category === filters.category
    );
  }

  // 💰 PRICE RANGE
  if (filters?.priceRange && filters.priceRange !== "All") {
    if (filters.priceRange === "0-500") {
      filteredItems = filteredItems.filter(item => item.price <= 500);
    } else if (filters.priceRange === "500-1000") {
      filteredItems = filteredItems.filter(
        item => item.price > 500 && item.price <= 1000
      );
    } else if (filters.priceRange === "1000+") {
      filteredItems = filteredItems.filter(item => item.price > 1000);
    }
  }

  // 🔄 SORTING
  if (filters?.sort === "lowToHigh") {
    filteredItems.sort((a, b) => a.price - b.price);
  } else if (filters?.sort === "highToLow") {
    filteredItems.sort((a, b) => b.price - a.price);
  } else if (filters?.sort === "aToZ") {
    filteredItems.sort((a, b) =>
      a.item.localeCompare(b.item)
    );
  }

  return (
    <div
      style={{
        background: "linear-gradient(to right, #16e7e4, #402d4a)",
        minHeight: "100vh",
        padding: "30px",
      }}
    >
      <h2 className="text-center text-light mb-4">
        All Products ({filteredItems.length})
      </h2>

      <ItemBoard items={filteredItems} user={user} />
    </div>
  );
};

export default HomePage;
