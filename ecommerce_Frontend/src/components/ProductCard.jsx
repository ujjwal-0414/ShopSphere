import { Link } from "react-router-dom";
import { addToCart } from "../api/cartApi";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import { getProductImage, handleProductImageError } from "./productImage";

export default function ProductCard({ product }) {
  const { refreshCart } = useCart();
  const { isAuthenticated } = useAuth();

  const handleAdd = async () => {
    if (!isAuthenticated) {
      window.location.href = "/login";
      return;
    }

    try {
      await addToCart(product.id, 1);
      await refreshCart();
      alert("Added to cart");
    } catch (error) {
      alert(error.response?.data?.message || "Could not add product to cart");
    }
  };

  return (
    <article className="product-card">
      <Link to={`/products/${product.id}`} className="product-image-wrap">
        <img
          src={getProductImage(product)}
          onError={(event) => handleProductImageError(event, product)}
          alt={product.name}
          loading="lazy"
        />
      </Link>

      <div className="product-card-body">
        <div className="eyebrow">{product.categoryName}</div>
        <Link to={`/products/${product.id}`} className="product-name">
          {product.name}
        </Link>
        <p className="product-desc">{product.description}</p>

        <div className="product-bottom">
          <strong>₹{Number(product.price).toLocaleString("en-IN")}</strong>
          <span className={product.stock > 0 ? "stock in" : "stock out"}>
            {product.stock > 0 ? `${product.stock} left` : "Out of stock"}
          </span>
        </div>

        <button
          className="btn btn-primary full"
          disabled={product.stock <= 0}
          onClick={handleAdd}
        >
          {product.stock > 0 ? "Add to Cart" : "Out of Stock"}
        </button>
      </div>
    </article>
  );
}
