import api from "./axios";

export const createPayment = (orderId, paymentMethod) =>
  api.post("/api/payments", { orderId, paymentMethod });

export const getPaymentByOrder = (orderId) =>
  api.get(`/api/payments/order/${orderId}`);
