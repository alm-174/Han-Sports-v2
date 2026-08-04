export const ORDER_STATUS_LIST = [
  "CHO_XAC_NHAN",
  "DANG_XU_LY",
  "DANG_GIAO",
  "HOAN_THANH",
  "DA_HUY",
];

export const ORDER_STATUS = {
  CHO_XAC_NHAN: { label: "Chờ xác nhận", color: "badge-blue", step: 0 },
  DANG_XU_LY: { label: "Đang xử lý", color: "badge-blue", step: 1 },
  DANG_GIAO: { label: "Đang giao", color: "badge-green", step: 2 },
  HOAN_THANH: { label: "Hoàn thành", color: "badge-green", step: 3 },
  DA_HUY: { label: "Đã hủy", color: "badge-danger", step: -1 },
  // Legacy statuses (đơn cũ trước khi migrate)
  PENDING: { label: "Chờ xác nhận", color: "badge-blue", step: 0 },
  PROCESSING: { label: "Đang xử lý", color: "badge-blue", step: 1 },
  SHIPPING: { label: "Đang giao", color: "badge-green", step: 2 },
  COMPLETED: { label: "Hoàn thành", color: "badge-green", step: 3 },
  CANCELLED: { label: "Đã hủy", color: "badge-danger", step: -1 },
};

export const ORDER_TIMELINE_STEPS = [
  { key: "CHO_XAC_NHAN", label: "Chờ xác nhận", icon: "hourglass_top" },
  { key: "DANG_XU_LY", label: "Đang xử lý", icon: "inventory_2" },
  { key: "DANG_GIAO", label: "Đang giao", icon: "local_shipping" },
  { key: "HOAN_THANH", label: "Hoàn thành", icon: "check_circle" },
];

const LEGACY_STATUS_MAP = {
  PENDING: "CHO_XAC_NHAN",
  PROCESSING: "DANG_XU_LY",
  SHIPPING: "DANG_GIAO",
  COMPLETED: "HOAN_THANH",
  CANCELLED: "DA_HUY",
};

export function normalizeOrderStatus(status) {
  if (!status) return "CHO_XAC_NHAN";
  return LEGACY_STATUS_MAP[status] || status;
}

export function getOrderStatusInfo(status) {
  const normalized = normalizeOrderStatus(status);
  return ORDER_STATUS[normalized] || { label: status || "N/A", color: "badge-blue", step: 0 };
}

export function canCancelOrder(status) {
  const normalized = normalizeOrderStatus(status);
  return normalized === "CHO_XAC_NHAN" || normalized === "DANG_XU_LY";
}

export function canConfirmOrder(status) {
  return normalizeOrderStatus(status) === "CHO_XAC_NHAN";
}

export function canPrintOrder(order) {
  return Boolean(order?.ghnOrderCode);
}

export function getTimelineStepIndex(status) {
  const info = getOrderStatusInfo(status);
  if (info.step < 0) return -1;
  return info.step;
}
