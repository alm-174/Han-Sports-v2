# Hansport_v2

Phiên bản demo của một cửa hàng (backend Spring Boot + frontend React + Vite).

## Demo
- Website: https://hansport.shop

### Tài khoản quản trị (Demo)
| Thông tin | Giá trị |
|-----------|----------|
| Email | `admin@hansport.local` |
| Mật khẩu | `Admin@123` |

> **Lưu ý:** Đây là tài khoản dành cho mục đích demo. Nếu triển khai thực tế hoặc công khai mã nguồn, hãy thay đổi mật khẩu hoặc xóa thông tin đăng nhập này khỏi README.

## Tổng quan
- Backend: `hansport_v2be` — Spring Boot (Java 17, Maven)
- Frontend: `hansport_v2fe` — React (Vite)

Ứng dụng gồm API cho sản phẩm, giỏ hàng, đặt hàng, xác thực (JWT + OAuth2 Google), gửi email, và lưu file upload.

## Cấu trúc dự án (chung)
- `hansport_v2be/` — mã nguồn backend, file cấu hình ở `hansport_v2be/src/main/resources/application.properties`
- `hansport_v2fe/` — mã nguồn frontend (Vite + React), các file tĩnh và thư mục `upload/` chứa ảnh

## Yêu cầu & môi trường
- Java 17
- Maven
- Node.js (phiên bản hiện tại: 18+ khuyến nghị)
- MySQL (hoặc cấu hình JDBC khác)

## Cấu hình quan trọng
- Database: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- JWT: `JWT_BASE64_SECRET`, `JWT_ACCESS_TOKEN_VALIDITY`, `JWT_REFRESH_TOKEN_VALIDITY`
- Upload path: `UPLOAD_FILE_BASE_PATH`
- OAuth2 Google: `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
- Email (Gmail): `spring.mail.username`, `spring.mail.password`

> **Lưu ý:** Không commit thông tin nhạy cảm (client secret, mật khẩu email, bí mật JWT) lên Git.

## Chạy backend (phát triển)

```bash
mvn clean install
mvn spring-boot:run
```

Hoặc:

```bash
mvn clean package
java -jar target/hansport_v2-0.0.1-SNAPSHOT.jar
```

Backend mặc định chạy trên cổng `8080`.

## Chạy frontend (phát triển)

```bash
npm install
npm run dev
```

Frontend mặc định chạy tại `http://localhost:5173`.

## Build production

### Backend

```bash
mvn clean package
```

### Frontend

```bash
npm run build
```

Thư mục `dist/` sẽ được tạo để deploy.

## Uploads & tài nguyên tĩnh

Backend mặc định lưu file vào:

```
hansport_v2fe/upload/
```

Thông qua biến:

```
UPLOAD_FILE_BASE_PATH
```

## Seed dữ liệu

Seeder được bật bằng:

```properties
app.seed.enabled=true
```

Dữ liệu mẫu sẽ được tạo khi ứng dụng khởi động.

## Troubleshooting

- Kiểm tra `DB_URL`, username, password nếu không kết nối được database.
- Kiểm tra cấu hình upload nếu upload ảnh lỗi.
- Kiểm tra `GOOGLE_CLIENT_ID` và `GOOGLE_CLIENT_SECRET` nếu Google Login không hoạt động.

## Thông tin thêm

- Backend: `hansport_v2be/pom.xml`
- Frontend: `hansport_v2fe/package.json`

---

## Công nghệ sử dụng

### Backend
- Spring Boot 3
- Spring Security
- Spring Data JPA
- JWT Authentication
- OAuth2 Google Login
- MySQL
- Maven

### Frontend
- React
- Vite
- React Router
- Axios
- Tailwind CSS / Bootstrap

## Tác giả

Phát triển bởi **HAN SPORTS**.