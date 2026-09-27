import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { getCategories, getProducts } from "../api/catalogApi";
import ProductCard from "../components/ProductCard";
import Loading from "../components/Loading";
import { getProductImage, handleProductImageError } from "../components/productImage";

export default function Home() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getProducts(), getCategories()])
      .then(([p, c]) => {
        setProducts(Array.isArray(p.data) ? p.data : []);
        setCategories(Array.isArray(c.data) ? c.data : []);
      })
      .catch(() => {
        setProducts([]);
        setCategories([]);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <section className="hero">
        <div className="container hero-grid">
          <div>
            <div className="eyebrow light">SHOPSPHERE E-COMMERCE</div>
            <h1>Everything you need.<br /><span>One simple store.</span></h1>
            <p>
              Browse products, manage your cart, save delivery addresses and
              complete your simulated checkout.
            </p>
            <div className="hero-actions">
              <Link to="/products" className="btn btn-primary btn-large">Explore Products</Link>
              <Link to="/register" className="btn btn-outline btn-large">Create Account</Link>
            </div>
          </div>

          <div className="hero-feature-card">
            {products[0] ? (
              <>
                <div className="hero-feature-image-wrap">
                  <img
                    src={getProductImage(products[0])}
                    onError={(event) => handleProductImageError(event, products[0])}
                    alt={products[0].name}
                  />
                  <span className="hero-feature-badge">FEATURED PICK</span>
                </div>
                <div className="hero-feature-body">
                  <div className="hero-feature-category">{products[0].categoryName}</div>
                  <h3>{products[0].name}</h3>
                  <div className="hero-feature-bottom">
                    <strong>₹{Number(products[0].price).toLocaleString("en-IN")}</strong>
                    <Link to={`/products/${products[0].id}`} className="hero-feature-link">View product →</Link>
                  </div>
                </div>
              </>
            ) : (
              <div className="hero-feature-loading">
                <div className="hero-card-icon">🛍️</div>
                <h3>Discover something new</h3>
                <p>Explore our collection of everyday products, electronics, books and more.</p>
                <Link to="/products" className="btn btn-primary">Browse Collection</Link>
              </div>
            )}
          </div>
        </div>
      </section>

      <main className="container section">
        <div className="section-head">
          <div>
            <div className="eyebrow">SHOP BY CATEGORY</div>
            <h2>Find what you need</h2>
          </div>
          <Link to="/products" className="text-link">View all →</Link>
        </div>

        <div className="category-grid">
          {categories.map(c => (
            <Link
              key={c.id}
              to={`/products?category=${encodeURIComponent(c.name)}`}
              className="category-card"
            >
              <span>{c.name?.slice(0, 1)}</span>
              <div>
                <strong>{c.name}</strong>
                <small>{c.description || "Browse products"}</small>
              </div>
            </Link>
          ))}
        </div>

        <div className="section-head top-gap">
          <div>
            <div className="eyebrow">OUR COLLECTION</div>
            <h2>Featured products</h2>
          </div>
        </div>

        {loading ? (
          <Loading />
        ) : products.length ? (
          <div className="product-grid">
            {products.slice(0, 8).map(p => <ProductCard key={p.id} product={p} />)}
          </div>
        ) : (
          <div className="state-card">
            <h3>No products available</h3>
            <p>Make sure the Spring Boot backend and database are running.</p>
          </div>
        )}
      </main>
    </>
  );
}
