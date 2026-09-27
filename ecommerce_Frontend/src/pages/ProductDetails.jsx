import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getProduct } from "../api/catalogApi";
import { addToCart } from "../api/cartApi";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";
import { getProductImage, handleProductImageError } from "../components/productImage";
import Loading from "../components/Loading";

export default function ProductDetails() {
  const { id } = useParams();
  const { isAuthenticated } = useAuth();
  const { refreshCart } = useCart();
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    getProduct(id)
      .then(r => setProduct(r.data))
      .catch(e => setError(e.response?.data?.message || "Product could not be loaded"))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return <main className="container section"><Loading /></main>;
  }

  if (error || !product) {
    return (
      <main className="container section">
        <div className="error-box">{error || "Product not found."}</div>
        <Link to="/products" className="btn btn-primary">Back to products</Link>
      </main>
    );
  }

  const add = async () => {
    if (!isAuthenticated) {
      window.location.href = "/login";
      return;
    }

    try {
      await addToCart(product.id, quantity);
      await refreshCart();
      alert("Added to cart");
    } catch (e) {
      alert(e.response?.data?.message || "Could not add product");
    }
  };

  return (
    <main className="container section">
      <Link to="/products" className="back-link">← Back to products</Link>

      <div className="detail-grid">
        <div className="detail-image">
          <img
            src={getProductImage(product)}
            onError={(event) => handleProductImageError(event, product)}
            alt={product.name}
            loading="eager"
            />
        </div>

        <div className="detail-copy">
          <div className="eyebrow">{product.categoryName}</div>
          <h1>{product.name}</h1>
          <div className="detail-price">
            ₹{Number(product.price).toLocaleString("en-IN")}
          </div>
          <p>{product.description}</p>
          <div className="detail-stock">
            {product.stock > 0 ? `${product.stock} units available` : "Out of stock"}
          </div>

          {product.stock > 0 && (
            <div className="qty-row">
              <button onClick={() => setQuantity(q => Math.max(1, q - 1))}>−</button>
              <span>{quantity}</span>
              <button onClick={() => setQuantity(q => Math.min(product.stock, q + 1))}>+</button>
            </div>
          )}

          <button
            className="btn btn-primary btn-large"
            disabled={!product.stock}
            onClick={add}
          >
            Add to Cart
          </button>
        </div>
      </div>
    </main>
  );
}
