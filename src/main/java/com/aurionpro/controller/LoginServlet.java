package com.aurionpro.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.model.User;
import com.aurionpro.service.UserService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username") != null ? request.getParameter("username").trim() : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
        String role = request.getParameter("role") != null ? request.getParameter("role").trim() : "";

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            request.setAttribute("errorMessage", "Please enter username, password and select role.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
            return;
        }

        User user = userService.validateUser(username, password, role);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("employeeName", user.getEmployeeName());
            if ("EMPLOYEE".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("employee.jsp");
            } else {
                response.sendRedirect("admin.jsp");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid credentials or role!");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
