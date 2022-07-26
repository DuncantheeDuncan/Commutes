package co_mute.scripts;

public class MysqlScripts {

    public static final String INSERT = "INSERT INTO co_mute.CarpoolOpportunity(Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,Email,Notes,Created_at) VALUES(?,?,?,?,?,?,?,?,?,?)";
    public static final String SEARCH = "SELECT * FROM co_mute.carpoolopportunity where Email =?";
    public static final String SEARCH_USER = "SELECT * FROM co_mute.registration where Email =?";
    public static final String SEARCH_JOINED = "SELECT * FROM co_mute.joinedcarpoolopportunity where JoinedBy =?";
    public static final String INSERT_USER = "INSERT INTO co_mute.Registration(first_name,last_name,Email,Phone_number,Password) VALUES(?,?,?,?,?)";
    public static final String UPDATE = "UPDATE co_mute.carpoolopportunity SET Available_seats= Available_seats +1 WHERE id =?";
    public static final String DELETE = "DELETE FROM co_mute.joinedcarpoolopportunity WHERE id=?";
    public static final String UPDATE_SEAT = "UPDATE co_mute.carpoolopportunity SET Available_seats= Available_seats -1 WHERE id =?";
    public static final String INSERT_CARPOOL = "INSERT INTO co_mute.JoinedCarpoolOpportunity(Departure,Expected_arrival,Origin,Days_available,Destination,Available_seats,Leader,JoinedBy,Email,Notes) VALUES(?,?,?,?,?,?,?,?,?,?)";
    public static final String SEARCH_CARPOOL = "SELECT * FROM co_mute.carpoolopportunity where id =?";
}
