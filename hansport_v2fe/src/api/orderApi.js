import axiosInstance from "./axiosSetup";

export const orderApi = {
  createOrder: (data) => axiosInstance.post("/api/v1/orders", data),
  checkout: (data) => axiosInstance.post("/api/v1/orders/checkout", data),
  getOrderById: (id) => axiosInstance.get(`/api/v1/orders/${id}`),
  getMyOrders: () => axiosInstance.get("/api/v1/orders/my"),
  getAllOrders: (params) => axiosInstance.get("/api/v1/orders", { params }),
  updateOrder: (data) => axiosInstance.put("/api/v1/orders", data),
  confirmOrder: (id) => axiosInstance.post(`/api/v1/orders/${id}/confirm`),
  printOrder: (id) => axiosInstance.post(`/api/v1/orders/${id}/print`),
  cancelOrder: (id) => axiosInstance.post(`/api/v1/orders/${id}/cancel`),
  deleteOrder: (id) => axiosInstance.delete(`/api/v1/orders/${id}`),
  sendOrderEmail: (id) => axiosInstance.get(`/api/v1/email/${id}`),
};
