package co_mute.worker;

import co_mute.crud.Create;
import co_mute.model.CarPool;
import co_mute.model.User;
import co_mute.scripts.MysqlScripts;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co_mute.dbcon.DB.EstablishDbConnection;

public class Worker {

    public static Map<String, Object> GET_USER = new HashMap<>();
    public static String DB_PASSWORD = null;
    private List<String> leader = new ArrayList<>();
    private List<Time> departure = new ArrayList<>();
    private List<String> destination = new ArrayList<>();
    private List<String> origin = new ArrayList<>();
    private List<Timestamp> created_at = new ArrayList<>();

    public static Boolean VerifyPassword(String fromFormPassword, String fromDbPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {

        return LoginWorker.validatePassword(fromFormPassword, fromDbPassword);
    }

    public static String Password(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return LoginWorker.generateStrongPasswordHash(password);
    }

    public String UpdateUser(String firstName, String secondName, String email, String phone) {
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE co_mute.Registration")
                .append(" ")
                .append("SET")
                .append(" ");
        sb.append("First_Name = '").append(firstName)
                .append("', ")
                .append("Last_Name ='").append(secondName)
                .append("', ")
                .append("Email ='").append(email)
                .append("', ")
                .append("Phone_number ='").append(phone)
                .append("' ")
                .append("WHERE Email =?");
        return sb.toString();
    }

    public boolean isUserExist(String email) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rst = null;
        Connection conn = null;

        try {
            // Lookup
            conn = EstablishDbConnection();
            stmt = conn.prepareStatement(MysqlScripts.SEARCH_USER);
            stmt.setString(1, email);
            rst = stmt.executeQuery();


            while (rst.next()) {
                if (email.equalsIgnoreCase(rst.getString("email"))) {

                    DB_PASSWORD = rst.getString("password");

                    GET_USER.put("firstName", rst.getString("First_Name"));
                    GET_USER.put("surName", rst.getString("Last_Name"));
                    GET_USER.put("email", rst.getString("Email"));
                    GET_USER.put("phone", rst.getString("Phone_number"));

                    return true;
                }
            }
        } catch (Exception e) {
            throw new Exception("Please create an account");
        }
        return false;
    }

    public List<String> getLeader() {
        return leader;
    }

    public List<Time> getDeparture() {
        return departure;
    }

    public List<String> getDestination() {
        return destination;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public List<Timestamp> getCreated_at() {
        return created_at;
    }


    public void GetJoinedPool(String loggedUser) throws Exception {
        ListCarPool(loggedUser).forEach((id, name) -> {
            leader.add(name.getLeader());
            departure.add(name.getDeparture());
            destination.add(name.getDestination());
            origin.add(name.getOrigin());
            created_at.add(name.getCreated_at());
        });
    }


    public Map<String, CarPool> ListCarPool(String email) throws Exception {

        Map<String, CarPool> map = new HashMap<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;

        try {
            con = EstablishDbConnection();
            stmt = con.prepareStatement(MysqlScripts.SEARCH_JOINED);
            stmt.setString(1, email);
            rst = stmt.executeQuery();
            int count = 0;

            while (rst.next()) {
                CarPool carPool = new CarPool(rst.getTime("departure"), rst.getString("origin"), rst.getString("destination"), rst.getString("leader"), rst.getTimestamp("Created_at"));
                map.put(carPool.getEmail() + count, carPool);
                count++;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return map;
    }

    public String CheckOverlappingTimes(CarPool carPool) {

        Create create = new Create();
        Connection con = null;
        //Search
        PreparedStatement stmt = null;
        ResultSet rst = null;

        try {
            con = EstablishDbConnection();
            stmt = con.prepareStatement(MysqlScripts.SEARCH);
            stmt.setString(1, carPool.getEmail());
            rst = stmt.executeQuery();

            if (!rst.isBeforeFirst()) {
                create.Insert(EstablishDbConnection(), carPool);
            } else {
                while (rst.next()) {
                    if (isTimeOverlap(carPool, rst)) {
                        create.Insert(EstablishDbConnection(), carPool);
                    } else {
                        return "The car pool cannot be created, It violates the rules";
                    }
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Car pool added successfully.";
    }


    public Boolean isTimeOverlap(CarPool carPool, ResultSet ResultSet) throws SQLException {

        int endH = Integer.parseInt(ResultSet.getString("Expected_arrival").split(":")[0]);
        int endM = Integer.parseInt(ResultSet.getString("Expected_arrival").split(":")[1]);

        int startH = Integer.parseInt(ResultSet.getString("Departure").split(":")[0]);
        int startM = Integer.parseInt(ResultSet.getString("Departure").split(":")[1]);

        StringBuilder timeline = new StringBuilder();

        for (int sh = startH; sh <= endH; sh++) {
            for (int sm = startM; sm <= 59; sm++) {
                if (sh > startH) {

                    for (int i = 1; i < sm; i++)
                        timeline.append(sh).append(":").append(i).append(",");

                    startH = sh;
                } else {
                    timeline.append(sh).append(":").append(sm).append(",");
                }

                if (sh == endH && sm == endM) {
                    // comparing values
                    String compareStart = carPool.getDeparture().toString().substring(0, 5);
                    String compareEnd = carPool.getExpected_arrival().toString().substring(0, 5);

                    String[] compare = timeline.toString().split(",");
                    for (String s : compare) {
                        if (s.equals(compareStart) || s.equals(compareEnd)) {
                            return false;
                        } else if (Time.valueOf(compareStart.concat(":0")).before(Time.valueOf(ResultSet.getString("Departure"))) && Time.valueOf(compareEnd.concat(":0")).after(Time.valueOf(ResultSet.getString("Expected_arrival")))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public String InsertUser(User user) throws SQLException {
        Create create = new Create();
        return create.InsertUser(EstablishDbConnection(), user);
    }
}
