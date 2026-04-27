package com.aurionpro.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.LeaveApplication;
import com.aurionpro.util.DBConnection;

public class LeaveDao {

    public void applyLeave(LeaveApplication leave) {
        String sql = "INSERT INTO leave_applications " +
                     "(employee_id, date_from, date_to, no_of_days, reason, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, leave.getEmployeeId());
            ps.setDate(2, new java.sql.Date(leave.getDateFrom().getTime()));
            ps.setDate(3, new java.sql.Date(leave.getDateTo().getTime()));
            ps.setInt(4, leave.getNoOfDays());
            ps.setString(5, leave.getReason());
            ps.setString(6, leave.getStatus() == null ? "PENDING" : leave.getStatus());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean hasOverlappingLeave(String employeeId, java.sql.Date from, java.sql.Date to) {
        String sql = "SELECT COUNT(*) FROM leave_applications " +
                     "WHERE employee_id=? " +
                     "AND status IN ('PENDING','APPROVED') " +
                     "AND (date_from <= ? AND date_to >= ?)";
        // Note: overlap when existing.from <= new.to AND existing.to >= new.from
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setDate(2, to);
            ps.setDate(3, from);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<LeaveApplication> getLeavesByEmployee(String employeeId) {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_applications WHERE employee_id=? ORDER BY date_from DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<LeaveApplication> getAllLeaves() {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_applications ORDER BY date_from DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void updateLeaveStatus(int leaveId, String status) {
        String sql = "UPDATE leave_applications SET status=? WHERE leave_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, leaveId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private LeaveApplication mapRow(ResultSet rs) throws SQLException {
        LeaveApplication leave = new LeaveApplication();
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getString("employee_id"));
        leave.setDateFrom(rs.getDate("date_from"));
        leave.setDateTo(rs.getDate("date_to"));
        leave.setNoOfDays(rs.getInt("no_of_days"));
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));
        return leave;
    }
}
