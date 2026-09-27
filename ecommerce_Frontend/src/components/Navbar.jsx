import { Link, NavLink } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const { itemCount } = useCart();

  return (
    <header className="navbar">
      <div className="container nav-inner">
        <Link to="/" className="brand">Shop<span>Sphere</span></Link>

        <nav className="nav-links">
          <NavLink to="/">Home</NavLink>
          <NavLink to="/products">Products</NavLink>
          {isAuthenticated && <NavLink to="/orders">Orders</NavLink>}
        </nav>

        <div className="nav-actions">
          {isAuthenticated && <Link className="cart-link" to="/cart">Cart <b>{itemCount}</b></Link>}
          {isAuthenticated ? (
            <>
              <Link className="user-chip" to="/profile">
                {user?.firstName || "Account"}
              </Link>
              <button className="btn btn-dark btn-small" onClick={logout}>Logout</button>
            </>
          ) : (
            <>
              <Link className="btn btn-ghost btn-small" to="/login">Login</Link>
              <Link className="btn btn-primary btn-small" to="/register">Register</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
