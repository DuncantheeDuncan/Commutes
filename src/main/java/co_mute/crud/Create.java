package co_mute.crud;

import co_mute.model.User;
import co_mute.scripts.MysqlScripts;

import java.sql.*;
import java.time.LocalDateTime;

import co_mute.model.CarPool;

public class Create {

    public String InsertUser(Connection conn, User user) {
        int status = 0;
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(MysqlScripts.INSERT_USER);

            pst.setString(1, user.getName());
            pst.setString(2, user.getSurname());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getPassword());
            status = pst.executeUpdate();


        } catch (Exception e) {
            return e.getMessage();
        }
        return status > 0 ? "User added successfully." : "User did not create a new field";
    }

    public String Insert(Connection conn, CarPool carPool) {
        PreparedStatement pst = null;
        int status = 0;

        try {
            pst = conn.prepareStatement(MysqlScripts.INSERT);

            pst.setTime(1, carPool.getDeparture());
            pst.setTime(2, carPool.getExpected_arrival());
            pst.setString(3, carPool.getOrigin());
            pst.setInt(4, carPool.getDays_available());
            pst.setString(5, carPool.getDestination());
            pst.setInt(6, carPool.getAvailable_seats());
            pst.setString(7, carPool.getLeader());
            pst.setString(8, carPool.getEmail());
            pst.setString(9, carPool.getNotes());
            pst.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            status = pst.executeUpdate();

        } catch (Exception e) {
            return e.getMessage();
        }

        return status > 0 ? "Car pool added successfully." : "Car pool did not create a new field";
    }
}
