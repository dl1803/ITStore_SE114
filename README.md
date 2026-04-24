# IT Store - Ứng dụng Thương mại Điện tử Linh kiện Điện tử

[![Android](https://img.shields.io/badge/Platform-Android%208.0+-3DDC84?logo=android&logoColor=white)](#)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?logo=java&logoColor=white)](#)
[![Status](https://img.shields.io/badge/Status-In_Progress-orange.svg)](#)

IT Store là ứng dụng Android Native phục vụ việc mua bán linh kiện điện tử (SSD, HDD, CPU, Mainboard, RAM, VGA, PSU...). Hệ thống được xây dựng với mục tiêu mang lại trải nghiệm mua sắm liền mạch cho khách hàng, đồng thời cung cấp công cụ quản lý kho hàng, đơn hàng và quy trình giao hàng toàn diện cho ban quản trị.

## Trải nghiệm Giao diện 

<p align="center">
  <img src="https://github.com/user-attachments/assets/4cd4beed-93dd-45e3-a8e5-cc3a799f7ba6" width="220" alt="Trang chủ"> &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/74180b03-666b-4eaa-a396-319a3275c85c" width="220" alt="Yêu thích"> &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/b98f3f83-ff28-426a-a630-26441d081ddd" width="220" alt="Giỏ hàng"> &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/22b93042-7aed-4663-bc5f-0f693441a0f4" width="220" alt="Hồ sơ">
</p>

## Tính năng Hệ thống

Hệ thống được chia thành các module chính dựa trên đối tượng sử dụng:

### 1. Phân hệ Khách hàng (Guest & Customer)
- Quản lý tài khoản: Đăng ký/Đăng nhập (Email, Google), xác thực email, quên mật khẩu (link hiệu lực 24h), quản lý địa chỉ giao hàng.
- Trải nghiệm mua sắm: Xem và tìm kiếm sản phẩm theo danh mục (CPU, Mainboard, RAM, VGA...), lọc theo giá và thông số.
- Quản lý Giỏ hàng: Thêm/sửa/xóa sản phẩm, áp dụng mã giảm giá/voucher.
- Thanh toán & Đặt hàng: Hỗ trợ thanh toán khi nhận hàng (COD) và thanh toán online (Momo, thẻ ATM).
- Theo dõi đơn hàng: Theo dõi 8 trạng thái đơn hàng (Từ "Chờ xác nhận" đến "Giao hàng thành công"), hủy đơn hàng, xác nhận nhận hàng.
- Tương tác: Thêm sản phẩm vào danh sách yêu thích, đánh giá sản phẩm sau khi mua.

### 2. Phân hệ Quản trị (Web Admin)
Lưu ý: Đây là hệ thống quản trị chạy trên nền tảng Web, được xây dựng để hỗ trợ quản lý dữ liệu cho ứng dụng Android:
- Quản lý Sản phẩm và Kho: Thêm, sửa, xóa sản phẩm, cập nhật số lượng tồn kho và nhận cảnh báo hàng sắp hết.
- Xử lý Đơn hàng: Tiếp nhận đơn từ App, xác nhận đơn, cập nhật trạng thái đóng gói.
- Quản lý Giao hàng: Phân công giao hàng, theo dõi tiến độ, cập nhật kết quả (thành công/thất bại) và xác nhận tiền thu (COD).
- Thống kê: Xem báo cáo doanh thu và sản phẩm bán chạy để điều phối hàng hóa.
- Cấu hình: Quản lý banner hiển thị trên App, mã giảm giá và thông báo hệ thống.

## Yêu cầu Kỹ thuật & Công nghệ

- Nền tảng: Android 8.0 trở lên.
- Ngôn ngữ: Java.
- Bảo mật: 
  - Mã hóa thông tin thanh toán và mật khẩu (bcrypt).
  - Xác thực người dùng qua JWT token.
  - Giao tiếp API bảo mật với HTTPS.
- Tích hợp (Third-party Services):
  - Firebase Cloud Messaging: Đẩy thông báo realtime.
  - Google Maps API: Quản lý địa chỉ và tính toán vận chuyển.
  - Payment Gateway: Tích hợp cổng thanh toán Momo / ZaloPay.
  - Email Service: Gửi email xác thực và đặt hàng (Sendgrid / Mailgun).

## Hướng dẫn Cài đặt (Local Development)

Để chạy dự án trên máy tính cá nhân:

1. Clone repository về máy bằng lệnh: git clone https://github.com/dl1803/ITStore_SE114.git
2. Mở Android Studio.
3. Chọn "Open an existing Android Studio project" và trỏ tới thư mục ITStore_SE114.
4. Chờ Gradle đồng bộ hóa (Sync) hoàn tất.
5. Setup file cấu hình API và các key tích hợp (Firebase, Momo) theo tài liệu nội bộ.
6. Khởi chạy trên máy ảo (Emulator) hoặc thiết bị Android (phiên bản 8.0+).

## Đội ngũ Phát triển
- Trần Lê Đức Lợi (dl1803)
- Nguyễn Đại Vương (vuongtapcode)
