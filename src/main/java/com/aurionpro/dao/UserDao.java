package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aurionpro.model.User;
import com.aurionpro.util.DBConnection;

public class UserDao {
	public User validateUser(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setEmployeeId(rs.getString("employee_id"));
                user.setEmployeeName(rs.getString("employee_name"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return user;
    }

	public User getUser(String username, String password, String role) {
		// TODO Auto-generated method stub
		return null;
	}
}

