import "./App.css";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useState, useEffect } from "react";

import NavBar from "./components/NavBar";
import ProtectedRoute from "./components/ProtectedRoute";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import CartPage from "./pages/CartPage";
import WishListPage from "./pages/WishListPage";
import OrderPage from "./pages/OrderPage";
import BillingPage from "./pages/BillingPage";
import ChatPage from "./pages/ChatPage";

function App() {
  const [user, setUser] = useState(null);

  // 🔥 Global Filter State
  const [filters, setFilters] = useState({
    search: "",
    category: "All",
    priceRange: "All",
    sort: "",
  });

  // 🔐 Restore login on refresh
  useEffect(() => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    const username = localStorage.getItem("username");

    if (token && userId) {
      setUser({ userId, username });
    }
  }, []);

  return (
    <Router>
      <Navbar
        user={user}
        setUser={setUser}
        filters={filters}
        setFilters={setFilters}
      />

      <Routes>

  {/* Home */}
  <Route
    path="/"
    element={<HomePage user={user} filters={filters} />}
  />

  {/* Login */}
  <Route
    path="/login"
    element={
      user ? <Navigate to="/" /> : <LoginPage setUser={setUser} />
    }
  />

  {/* Signup */}
  <Route
    path="/signup"
    element={
      user ? <Navigate to="/" /> : <SignupPage />
    }
  />

  {/* 🔒 Protected Cart */}
  <Route
    path="/cart"
    element={
      <ProtectedRoute user={user}>
        <CartPage user={user} />
      </ProtectedRoute>
    }
  />

  {/* 🔒 Protected Wishlist */}
  <Route
    path="/wishlist"
    element={
      <ProtectedRoute user={user}>
        <WishListPage user={user} />
      </ProtectedRoute>
    }
  />

  {/* 🔒 Protected Order */}
  <Route
    path="/order"
    element={
      <ProtectedRoute user={user}>
        <OrderPage user={user} />
      </ProtectedRoute>
    }
  />
  {/* 🔒 Protected Billing */}
  <Route
  path="/bill"
  element={
    <ProtectedRoute user={user}>
      <BillingPage />
    </ProtectedRoute>
  }
/>
<Route
  path="/chat"
  element={
    <ProtectedRoute user={user}>
      <ChatPage user={user} />
    </ProtectedRoute>
  }
/>

  {/* Redirect unknown routes */}
  <Route path="*" element={<Navigate to="/" />} />

</Routes>
    </Router>
  );
}

export default App;
