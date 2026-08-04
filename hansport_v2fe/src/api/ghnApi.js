import axiosInstance, { axiosPublic } from "./axiosSetup";

export const ghnApi = {
  getProvinces: () => axiosPublic.get("/api/v1/ghn/provinces"),
  getDistricts: (provinceId) => axiosPublic.post("/api/v1/ghn/districts", { id: provinceId }),
  getWards: (districtId) => axiosPublic.post("/api/v1/ghn/wards", { id: districtId }),
  getFee: (payload) => axiosPublic.post("/api/v1/ghn/fee", payload),
  createOrder: (payload) => axiosPublic.post("/api/v1/ghn/create-order", payload),
  getOrderDetail: (orderCode) => axiosPublic.post("/api/v1/ghn/detail", { orderCode }),
  getOrderDetailByClientCode: (clientOrderCode) => axiosPublic.post("/api/v1/ghn/detail-by-client-code", { clientOrderCode }),
  cancelOrders: (orderCodes) => axiosPublic.post("/api/v1/ghn/cancel", { orderCodes }),
};
