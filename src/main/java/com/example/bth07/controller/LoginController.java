package com.example.bth07.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bth07.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        // Nếu người dùng đã có session (đã đăng nhập) thì chuyển hướng về trang chủ
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/home";
        }
        return "login"; // Hiển thị trang login
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            // Không tìm thấy user
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }

        // So sánh mật khẩu
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // Mật khẩu khớp, lưu thông tin người dùng vào session
            session.setAttribute("currentUser", userDetails);
            return "redirect:/home";
        } else {
            // Mật khẩu không khớp
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        return "redirect:/login";
    }
}
