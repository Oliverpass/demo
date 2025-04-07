package com.evn.demo.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class SensorDataService {

    public void saveSensorData(String topic, String payload) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://154.21.200.171:3306/air_monitoring_system", "root", "mysql_m7Ynww")) {
            String sql = "INSERT INTO tioilet (topic, payload) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, topic);
                statement.setString(2, payload);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
