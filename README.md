# Hansport_v2

Phiên bản demo của một cửa hàng (backend Spring Boot + frontend React + Vite).

## Tổng quan
- Backend: `hansport_v2be` — Spring Boot (Java 17, Maven)
- Frontend: `hansport_v2fe` — React (Vite)

Ứng dụng gồm API cho sản phẩm, giỏ hàng, đặt hàng, xác thực (JWT + OAuth2 Google), gửi email, và lưu file upload.

## Cấu trúc dự án (chung)
- `hansport_v2be/` — mã nguồn backend, file cấu hình ở [hansport_v2be/src/main/resources/application.properties](hansport_v2be/src/main/resources/application.properties)
- `hansport_v2fe/` — mã nguồn frontend (Vite + React), các file tĩnh và thư mục `upload/` chứa ảnh

## Yêu cầu & môi trường
- Java 17
- Maven
- Node.js (phiên bản hiện tại: 18+ khuyến nghị)
- MySQL (hoặc cấu hình JDBC khác)

## Cấu hình quan trọng
- Database: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (cấu hình mặc định có trong [hansport_v2be/src/main/resources/application.properties](hansport_v2be/src/main/resources/application.properties))
- JWT: `JWT_BASE64_SECRET`, `JWT_ACCESS_TOKEN_VALIDITY`, `JWT_REFRESH_TOKEN_VALIDITY`
- Upload path: `UPLOAD_FILE_BASE_PATH` — mặc định trỏ sang thư mục `../hansport_v2fe/upload`
- OAuth2 Google: `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
- Email (Gmail): `spring.mail.username`, `spring.mail.password`

Lưu ý: không commit thông tin nhạy cảm (client secret, mật khẩu email, bí mật JWT) vào git.

## Chạy backend (phát triển)
1. Cấu hình MySQL và tạo database nếu cần.
2. Từ thư mục `hansport_v2be`:

```bash
mvn clean install
mvn spring-boot:run
```

Hoặc build JAR và chạy:

```bash
mvn clean package
java -jar target/hansport_v2-0.0.1-SNAPSHOT.jar
```

Ứng dụng mặc định chạy trên cổng `8080` (có thể thay bằng biến `SERVER_PORT`).

## Chạy frontend (phát triển)
1. Vào thư mục `hansport_v2fe`:

```bash
npm install
npm run dev
```

Trang frontend mặc định khởi chạy trên `http://localhost:5173`.

## Build production
- Backend: `mvn clean package` → deploy JAR/war
- Frontend: trong `hansport_v2fe` chạy `npm run build` → folder `dist` để deploy

## Uploads & tài nguyên tĩnh
- Thư mục upload frontend: `hansport_v2fe/upload/` — backend mặc định lưu file vào thư mục này (tham số `UPLOAD_FILE_BASE_PATH`).

## Seed dữ liệu
- Có seeder được bật bằng `app.seed.enabled=true` trong file cấu hình để chèn dữ liệu mẫu khi ứng dụng khởi động. Kiểm tra class `DataSeeder` trong `hansport_v2be/src/main/java/com/javaweb/config`.

## Troubleshooting nhanh
- Lỗi kết nối DB: kiểm tra `DB_URL`, user, password và quyền truy cập.
- Lỗi upload: kiểm tra `spring.servlet.multipart.max-file-size` và `max-request-size` trong `application.properties`.
- OAuth/Google login: chắc chắn `GOOGLE_CLIENT_ID` và `GOOGLE_CLIENT_SECRET` đã cấu hình đúng và callback URL khớp với frontend.

## Thông tin thêm
- Pom backend: [hansport_v2be/pom.xml](hansport_v2be/pom.xml)
- Frontend package: [hansport_v2fe/package.json](hansport_v2fe/package.json)

---
Nếu bạn muốn, tôi có thể:
- Tạo file `.env.example` cho backend và frontend chứa các biến môi trường cần thiết.
- Tự động ẩn các secrets khỏi `application.properties` và hướng dẫn dùng biến môi trường.

Liên hệ: người phát triển trong repo.
