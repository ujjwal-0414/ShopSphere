import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getCategories, getProducts, getProductsByCategory, searchProducts } from "../api/catalogApi";
import ProductCard from "../components/ProductCard";
import Loading from "../components/Loading";
import EmptyState from "../components/EmptyState";

export default function Products() {
  const [params, setParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [search, setSearch] = useState(params.get("search") || "");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const category = params.get("category") || "";

  useEffect(() => {
    getCategories().then(r => setCategories(r.data || [])).catch(() => {});
  }, []);

  useEffect(() => {
    setLoading(true);
    setError("");
    const request = search.trim()
      ? searchProducts(search.trim())
      : category
        ? getProductsByCategory(category)
        : getProducts();

    request
      .then(r => setProducts(r.data || []))
      .catch(e => setError(e.response?.data?.message || "Could not load products"))
      .finally(() => setLoading(false));
  }, [category, search]);

  const submitSearch = (e) => {
    e.preventDefault();
    const next = new URLSearchParams(params);
    if (search.trim()) next.set("search", search.trim());
    else next.delete("search");
    next.delete("category");
    setParams(next);
  };

  return (
    <main className="container section">
      <div className="page-title">
        <div>
          <div className="eyebrow">CATALOG</div>
          <h1>All products</h1>
        </div>
        <form className="search" onSubmit={submitSearch}>
          <input value={search} onChange={e => setSearch(e.target.value)} placeholder="Search products..." />
          <button className="btn btn-dark">Search</button>
        </form>
      </div>

      <div className="filter-row">
        <button className={!category ? "filter active" : "filter"} onClick={() => setParams({})}>All</button>
        {categories.map(c => (
          <button key={c.id} className={category === c.name ? "filter active" : "filter"}
            onClick={() => setParams({ category: c.name })}>{c.name}</button>
        ))}
      </div>

      {loading && <Loading text="Loading products from Spring Boot..." />}
      {!loading && error && <EmptyState title="Something went wrong" text={error} />}
      {!loading && !error && products.length === 0 && <EmptyState title="No products found" text="Try another search or category." />}
      {!loading && !error && products.length > 0 && (
        <div className="product-grid">{products.map(p => <ProductCard key={p.id} product={p} />)}</div>
      )}
    </main>
  );
}
