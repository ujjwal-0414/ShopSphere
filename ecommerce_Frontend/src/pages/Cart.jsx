import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCart, updateCartItem, removeCartItem, clearCart } from "../api/cartApi";
import { getProductImage, handleProductImageError } from "../components/productImage";
import Loading from "../components/Loading";

export default function Cart() {
  const [cart, setCart] = useState({ items: [], totalPrice: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadCart = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await getCart();
      const data = response?.data || {};

      setCart({
        ...data,
        items: Array.isArray(data.items) ? data.items : [],
        totalPrice: data.totalPrice ?? 0
      });
    } catch (e) {
      setError(e.response?.data?.message || "Could not load your cart.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  const update = async (productId, quantity) => {
    try {
      const response = await updateCartItem(productId, quantity);
      const data = response?.data || {};
      setCart({
        ...data,
        items: Array.isArray(data.items) ? data.items : [],
        totalPrice: data.totalPrice ?? 0
      });
    } catch (e) {
      alert(e.response?.data?.message || "Could not update cart");
    }
  };

  const remove = async (productId) => {
    try {
      await removeCartItem(productId);
      await loadCart();
    } catch (e) {
      alert(e.response?.data?.message || "Could not remove item");
    }
  };

  const clear = async () => {
    if (!window.confirm("Clear your cart?")) return;

    try {
      await clearCart();
      await loadCart();
    } catch (e) {
      alert(e.response?.data?.message || "Could not clear cart");
    }
  };

  if (loading) {
    return (
      <main className="container section">
        <Loading text="Loading your cart..." />
      </main>
    );
  }

  if (error) {
    return (
      <main className="container section">
        <div className="page-title">
          <div>
            <div className="eyebrow">YOUR BAG</div>
            <h1>Shopping cart</h1>
          </div>
        </div>
        <div className="error-box">{error}</div>
        <button className="btn btn-primary" onClick={loadCart}>Try again</button>
      </main>
    );
  }

  const items = Array.isArray(cart.items) ? cart.items : [];

  return (
    <main className="container section">
      <div className="page-title">
        <div>
          <div className="eyebrow">YOUR BAG</div>
          <h1>Shopping cart</h1>
        </div>
        {items.length > 0 && (
          <button className="btn btn-ghost" onClick={clear}>Clear cart</button>
        )}
      </div>

      {!items.length ? (
        <div className="state-card">
          <div className="empty-cart-icon">🛒</div>
          <h3>Your cart is empty</h3>
          <p>Add a few products to continue shopping.</p>
          <Link to="/products" className="btn btn-primary">Browse products</Link>
        </div>
      ) : (
        <div className="cart-layout">
          <div className="cart-list">
            {items.map(item => (
              <div className="cart-item" key={item.id}>
                <img
                  src={getProductImage({
                    imageUrl: item.imageUrl,
                    categoryName: ""
                  })}
                  onError={(event) => handleProductImageError(event, { categoryName: "" })}
                  alt={item.productName}
                  loading="lazy"
                        />

                <div className="cart-info">
                  <Link to={`/products/${item.productId}`}>
                    {item.productName}
                  </Link>
                  <small>
                    ₹{Number(item.price ?? 0).toLocaleString("en-IN")}
                  </small>
                </div>

                <div className="qty-row compact">
                  <button
                    onClick={() =>
                      update(item.productId, Math.max(1, Number(item.quantity || 1) - 1))
                    }
                  >−</button>
                  <span>{item.quantity}</span>
                  <button
                    onClick={() =>
                      update(item.productId, Number(item.quantity || 1) + 1)
                    }
                  >+</button>
                </div>

                <strong>
                  ₹{Number(item.subtotal ?? 0).toLocaleString("en-IN")}
                </strong>

                <button
                  className="icon-btn"
                  aria-label={`Remove ${item.productName}`}
                  onClick={() => remove(item.productId)}
                >×</button>
              </div>
            ))}
          </div>

          <aside className="summary">
            <h3>Order summary</h3>
            <div>
              <span>Subtotal</span>
              <strong>₹{Number(cart.totalPrice ?? 0).toLocaleString("en-IN")}</strong>
            </div>
            <div><span>Shipping</span><strong>Free</strong></div>
            <hr />
            <div className="total">
              <span>Total</span>
              <strong>₹{Number(cart.totalPrice ?? 0).toLocaleString("en-IN")}</strong>
            </div>
            <Link to="/checkout" className="btn btn-primary full">
              Continue to checkout
            </Link>
          </aside>
        </div>
      )}
    </main>
  );
}
