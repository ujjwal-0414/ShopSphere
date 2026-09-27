import api from "./axios";

export const getProducts = () => api.get("/api/product/allProducts");
export const getProduct = (id) => api.get(`/api/product/${id}`);
export const searchProducts = (name) => api.get("/api/product/search", { params: { name } });
export const getProductsByCategory = (categoryName) =>
  api.get(`/api/product/category/${encodeURIComponent(categoryName)}`);
export const getProductsByPrice = (minPrice, maxPrice) =>
  api.get("/api/product/price", { params: { minPrice, maxPrice } });

export const getCategories = () => api.get("/api/category/allCategories");
