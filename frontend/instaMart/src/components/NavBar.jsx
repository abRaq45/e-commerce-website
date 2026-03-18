import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";

const Navbar = ({ user, setUser, filters, setFilters }) => {
  const navigate = useNavigate();
  const [showFilter, setShowFilter] = useState(false);

  const handleLogout = () => {
    localStorage.clear();
    setUser(null);
    navigate("/login");
  };

  return (
    <>
      {/* ================= NAVBAR ================= */}
      <nav className="navbar navbar-dark bg-dark px-4 py-3 shadow">
        <Link className="navbar-brand fw-bold fs-4 text-warning" to="/">
          InstaMart 🛒
        </Link>

        <div className="d-flex align-items-center gap-3">

          {/* 🔍 FILTER BUTTON (Always Visible) */}
          <button
            className="btn btn-outline-light"
            onClick={() => setShowFilter(true)}
          >
            ⚙ Filters
          </button>

          {/* 🔥 LOGGED-IN USER BUTTONS */}
          {user && (
            <>
              <Link className="btn btn-outline-info" to="/cart">
                🛒 Cart
              </Link>

              <Link className="btn btn-outline-danger" to="/wishlist">
                ❤️ Wishlist
              </Link>

              <Link className="btn btn-outline-primary" to="/chat">
                Chatbot
              </Link>

              <button
                className="btn btn-warning"
                onClick={handleLogout}
              >
                Logout
              </button>
            </>
          )}

          {/* 🔥 NOT LOGGED-IN BUTTONS */}
          {!user && (
            <>
              <Link className="btn btn-outline-light" to="/login">
                Login
              </Link>

              <Link className="btn btn-success" to="/signup">
                Signup
              </Link>
            </>
          )}

        </div>
      </nav>

      {/* ================= FILTER OVERLAY ================= */}
      {showFilter && (
        <div
          className="filter-overlay"
          onClick={() => setShowFilter(false)}
        ></div>
      )}

      {/* ================= FILTER DRAWER ================= */}
      <div className={`filter-drawer ${showFilter ? "open" : ""}`}>
        <div className="filter-header">
          <h5 className="fw-bold">Filters</h5>
          <button
            className="btn-close"
            onClick={() => setShowFilter(false)}
          ></button>
        </div>

        <div className="filter-body">

          {/* 🔎 SEARCH */}
          <input
            type="text"
            className="form-control mb-3"
            placeholder="Search product..."
            value={filters.search}
            onChange={(e) =>
              setFilters({ ...filters, search: e.target.value })
            }
          />

          {/* 📂 CATEGORY */}
          <select
            className="form-select mb-3"
            value={filters.category}
            onChange={(e) =>
              setFilters({ ...filters, category: e.target.value })
            }
          >
            <option value="All">All Categories</option>
            <option value="Electronics">Electronics</option>
            <option value="Clothing">Clothing</option>
            <option value="Fruits">Fruits</option>
            <option value="Vegetables">Vegetables</option>
            <option value="Grocery">Grocery</option>
          </select>

          {/* 💰 PRICE */}
          <select
            className="form-select mb-3"
            value={filters.priceRange}
            onChange={(e) =>
              setFilters({ ...filters, priceRange: e.target.value })
            }
          >
            <option value="All">All Prices</option>
            <option value="0-500">₹ 0 - 500</option>
            <option value="500-1000">₹ 500 - 1000</option>
            <option value="1000+">₹ 1000+</option>
          </select>

          {/* 🔄 SORT */}
          <select
            className="form-select mb-4"
            value={filters.sort}
            onChange={(e) =>
              setFilters({ ...filters, sort: e.target.value })
            }
          >
            <option value="">Sort By</option>
            <option value="lowToHigh">Price: Low → High</option>
            <option value="highToLow">Price: High → Low</option>
            <option value="aToZ">Name: A → Z</option>
          </select>

          {/* 🔁 RESET */}
          <button
            className="btn btn-outline-secondary w-100"
            onClick={() =>
              setFilters({
                search: "",
                category: "All",
                priceRange: "All",
                sort: ""
              })
            }
          >
            Reset Filters
          </button>

        </div>
      </div>
    </>
  );
};

export default Navbar;
