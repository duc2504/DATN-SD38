//
//
//
//package com.example.datn.Config;
//
//import com.example.datn.Model.Role;
//import com.example.datn.Model.Users;
//import com.example.datn.Repository.RoleRepository;
//import com.example.datn.Repository.UsersRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JwtUtil jwtUtil;
//    private final UsersRepository usersRepository;
//    private final RoleRepository roleRepository;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//        // Tìm user trong DB hoặc tạo mới
//        Users user = usersRepository.findByUsername(email)
//                .orElseGet(() -> {
//                    Role role = roleRepository.findByRoleName("USER")
//                            .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
//                    Users newUser = new Users();
//                    newUser.setUsername(email);
//                    newUser.setHoTen(name);
//                    newUser.setEmail(email);
//                    newUser.setPasswords(UUID.randomUUID().toString());
//                    newUser.setRole(role);
//
//                    // set tenHienThi mặc định từ email
//                    String tenHienThi = (email != null && email.contains("@"))
//                            ? email.substring(0, email.indexOf("@"))
//                            : (email != null ? email : "user");
//                    newUser.setTenHienThi(tenHienThi);
//
//                    return usersRepository.save(newUser);
//                });
//
//        // Nếu user cũ chưa có tenHienThi thì bổ sung
//        if (user.getTenHienThi() == null || user.getTenHienThi().isBlank()) {
//            String tenHienThi = (email != null && email.contains("@"))
//                    ? email.substring(0, email.indexOf("@"))
//                    : (email != null ? email : "user");
//            user.setTenHienThi(tenHienThi);
//            usersRepository.save(user);
//        }
//
//        String role = user.getRole().getRoleName();
//        String token = jwtUtil.generateToken(user.getUsername(), role);
//
//        // Encode để tránh null
//        String encodedTenHienThi = URLEncoder.encode(
//                user.getTenHienThi() != null ? user.getTenHienThi() : "user",
//                StandardCharsets.UTF_8
//        );
//
//        String redirectUrl = "http://localhost:5173/login?token=" + token +
//                "&role=" + role +
//                "&username=" + user.getUsername() +
//                "&tenHienThi=" + encodedTenHienThi;
//
//        response.sendRedirect(redirectUrl);
//    }
//}
