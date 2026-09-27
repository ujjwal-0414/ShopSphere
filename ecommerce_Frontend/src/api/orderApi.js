import api from "./axios";

export const placeOrder = (addressId) =>
  api.post("/api/orders", { addressId });

export const getOrders = () => api.get("/api/orders");
export const getOrder = (id) => api.get(`/api/orders/${id}`);
export const cancelOrder = (id) => api.put(`/api/orders/${id}/cancel`);
