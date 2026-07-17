const CLOUDINARY_CLOUD_NAME = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME;
const CLOUDINARY_UPLOAD_FOLDER_PREFIX = import.meta.env.VITE_CLOUDINARY_UPLOAD_FOLDER_PREFIX || "hansport_v2";

function toCloudinaryUrl(folder, fileName) {
  if (!fileName) return null;
  if (fileName.startsWith("http") || fileName.startsWith("data:")) return fileName;
  if (!CLOUDINARY_CLOUD_NAME) return null;

  return `https://res.cloudinary.com/${CLOUDINARY_CLOUD_NAME}/image/upload/${CLOUDINARY_UPLOAD_FOLDER_PREFIX}/${folder}/${fileName}`;
}

// ─── Logo assets ───
export const LOGO_CIRCLE = toCloudinaryUrl("logo", "z7807481637936_0284e7519d48b7526c7093c9e370821b.jpg");
export const LOGO_TEXT = toCloudinaryUrl("logo", "z7807481884127_d5f1ae90f114ea8f06f081653cf869fc.jpg");

// ─── Utility functions ───
export function getImageUrl(fileName, folder = "product") {
  return toCloudinaryUrl(folder, fileName);
}

export function getFirstImage(item) {
  if (!item) return null;
  // if already a string (filename/url)
  if (typeof item === "string") return item;
  // if object has images array
  if (Array.isArray(item.images) && item.images.length > 0) {
    const first = item.images[0];
    return typeof first === "string" ? first : (first.imageUrl || first.image || null);
  }
  // fallback to single image property
  if (item.image) return item.image;
  return null;
}

export function formatVND(amount) {
  if (!amount && amount !== 0) return "—";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    maximumFractionDigits: 0,
  }).format(amount);
}

export function formatDate(dateStr) {
  if (!dateStr) return "—";
  return new Intl.DateTimeFormat("vi-VN", {
    day: "2-digit", month: "2-digit", year: "numeric",
    hour: "2-digit", minute: "2-digit",
  }).format(new Date(dateStr));
}

export function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

export const ORDER_STATUS = {
  PENDING: { label: "Chờ xác nhận", color: "badge-blue" },
  PROCESSING: { label: "Đang xử lý", color: "badge-blue" },
  SHIPPING: { label: "Đang giao", color: "badge-green" },
  COMPLETED: { label: "Hoàn thành", color: "badge-green" },
  CANCELLED: { label: "Đã hủy", color: "badge-danger" },
};
