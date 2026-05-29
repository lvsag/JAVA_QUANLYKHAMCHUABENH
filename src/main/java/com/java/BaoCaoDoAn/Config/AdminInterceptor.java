package com.java.BaoCaoDoAn.Config;

import com.java.BaoCaoDoAn.Model.TaiKhoan;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        String role = (String) session.getAttribute("role");

        if (user == null || role == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (!role.equals("ADMIN") && !role.equals("BAC_SI")) {
            response.sendRedirect("/home");
            return false;
        }

        return true;
    }
}
