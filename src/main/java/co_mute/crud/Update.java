package co_mute.crud;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static co_mute.dbcon.DB.EstablishDbConnection;

public class Update {

    public String upDateUser(String email, String updateUser) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;
        try {
            con = EstablishDbConnection();
            //Update user
            stmt = con.prepareStatement(updateUser);
            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User has been Updated successfully.";
    }
}
