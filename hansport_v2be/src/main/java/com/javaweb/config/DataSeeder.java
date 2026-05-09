package com.javaweb.config;

import com.javaweb.domain.Product;
import com.javaweb.domain.Role;
import com.javaweb.domain.User;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

//    @Value("${app.seed.enabled:true}")
//    private boolean seedEnabled;
//
//    @Bean
//    CommandLineRunner seedDemoData(RoleRepository roleRepository,
//                                   UserRepository userRepository,
//                                   ProductRepository productRepository,
//                                   PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (!seedEnabled) {
//                return;
//            }
//
//            Role adminRole = roleRepository.findByName("ADMIN")
//                    .orElseGet(() -> roleRepository.save(createRole("ADMIN", "Quản trị hệ thống")));
//            Role userRole = roleRepository.findByName("USER")
//                    .orElseGet(() -> roleRepository.save(createRole("USER", "Khách hàng")));
//
//            if (!userRepository.existsByEmail("admin@hansport.local")) {
//                User admin = createUser("admin@hansport.local", "Admin@123", "HanSport Admin",
//                        "Hồ Chí Minh", "0900000001", adminRole, passwordEncoder);
//                userRepository.save(admin);
//            }
//
//            if (!userRepository.existsByEmail("user@hansport.local")) {
//                User user = createUser("user@hansport.local", "User@123", "Nguyễn Văn Demo",
//                        "Hà Nội", "0900000002", userRole, passwordEncoder);
//                userRepository.save(user);
//            }
//
//            seedProduct(productRepository, "Giày đá bóng 11Play Pro", 890000,
//                    "Giày đá bóng sân cỏ nhân tạo, form ôm chân, đế bám tốt khi đổi hướng.",
//                    "Giày sân cỏ nhân tạo bám sân tốt", 25, "11Play", "Nam",
//                    "1777975941237-11play-2.jpg");
//            seedProduct(productRepository, "Áo training HanSport DryFit", 240000,
//                    "Áo tập chất liệu nhanh khô, nhẹ, phù hợp chạy bộ và tập gym.",
//                    "Áo tập nhanh khô", 60, "HanSport", "Unisex",
//                    null);
//            seedProduct(productRepository, "Bóng đá tiêu chuẩn HS5", 320000,
//                    "Bóng đá size 5 dùng cho luyện tập và thi đấu phong trào.",
//                    "Bóng đá size 5", 40, "HanSport", "Unisex",
//                    null);
//            seedProduct(productRepository, "Găng tay thủ môn GripMax", 450000,
//                    "Găng tay thủ môn có lớp latex bám bóng, cổ tay chắc chắn.",
//                    "Găng tay thủ môn bám bóng", 18, "GripMax", "Nam",
//                    null);
//        };
//    }
//
//    private Role createRole(String name, String description) {
//        Role role = new Role();
//        role.setName(name);
//        role.setDecription(description);
//        return role;
//    }
//
//    private User createUser(String email, String password, String fullName, String address,
//                            String phone, Role role, PasswordEncoder passwordEncoder) {
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setFullName(fullName);
//        user.setAddress(address);
//        user.setPhone(phone);
//        user.setRole(role);
//        return user;
//    }
//
//    private void seedProduct(ProductRepository productRepository, String name, double price,
//                             String detailDesc, String shortDesc, long quantity,
//                             String brand, String target, String image) {
//        if (productRepository.existsByName(name)) {
//            return;
//        }
//        Product product = new Product();
//        product.setName(name);
//        product.setPrice(price);
//        product.setDetailDesc(detailDesc);
//        product.setShortDesc(shortDesc);
//        product.setQuantity(quantity);
//        product.setSold(0);
//        product.setBrand(brand);
//        product.setTarget(target);
//        product.setImage(image);
//        productRepository.save(product);
//    }
}

