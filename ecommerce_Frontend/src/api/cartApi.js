import api from "./axios";

export const getCart = () => api.get("/api/cart/all");
export const addToCart = (productId, quantity = 1) =>
  api.post("/api/cart/add", { productId, quantity });
export const updateCartItem = (productId, quantity) =>
  api.put(`/api/cart/items/${productId}`, { quantity });
export const removeCartItem = (productId) =>
  api.delete(`/api/cart/items/${productId}`);
export const clearCart = () => api.delete("/api/cart");
