import co_mute.crud.Update;
import co_mute.dbcon.DB;
import co_mute.model.CarPool;
import co_mute.model.User;
import co_mute.scripts.MysqlScripts;
import co_mute.worker.Worker;
import co_mute.worker.WorkerController;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.*;
import java.util.*;

import static co_mute.dbcon.DB.EstablishDbConnection;
import static spark.Spark.*;


public class Routes {

    public static Boolean AUTHENTICATED = false;
    public static String LOGGED_USER = "";

    public static void main(String[] args) throws Exception {

        // create connection
        EstablishDbConnection();
        new DB().RunSqlScrip(EstablishDbConnection());

        //Join/ leave carpool
        //Join
        new Routes().JoinCarPoolOpportunity(1, "carPool.getEmail()");
        new Routes().LeaveCarPoolOpportunity(1);


//            port(8080);
        staticFiles.location("/public");

        post("/profile/update", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            String UPDATE = new Worker().UpdateUser(req.queryParams("firstName"), req.queryParams("surName"), LOGGED_USER, req.queryParams("phone"));
            new Update().upDateUser(req.queryParams("email"), UPDATE);
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/main.handlebars"));
        });

        get("/profile/update", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            if (AUTHENTICATED) {
                map.put("user", Worker.GET_USER);
                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/profileupdate.hbs"));
            }
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
        });

        get("/profile", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            if (AUTHENTICATED) {
                map.put("user", Worker.GET_USER);
                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/profile.hbs"));
            }
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
        });

        get("/", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            if (AUTHENTICATED) {
                WorkerController wc = new WorkerController();
                map.put("user", wc.GetListCarPool(LOGGED_USER));

                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/main.handlebars"));
            }
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));

        });

        post("/login", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            WorkerController wc = new WorkerController();

            if (wc.connect(req.queryParams("email"), req.queryParams("password"))) {
                AUTHENTICATED = true;
                LOGGED_USER = req.queryParams("email");
                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/main.handlebars"));
            } else {
                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
            }

        });
        get("/login", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
        });

        // Using string/html
        notFound("<html><body><h1>404<br>The page you are looking for does not exist</h1><a href='/'>goto home</a></body></html>");

        post("/create/user", (req, res) -> {
            Map<String, Object> map = new HashMap<>();

            WorkerController wc = new WorkerController();
            User user = new User(req.queryParams("firstName"), req.queryParams("lastName"), req.queryParams("email"), req.queryParams("phone"), req.queryParams("password"));
            wc.AddUser(user);
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
        });

        get("/create/user", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/create.hbs"));
        });

        get("/register", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            if (AUTHENTICATED)
                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/register.hbs"));

            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/login.handlebars"));
        });

        post("/register", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            WorkerController wc = new WorkerController();

            CarPool carPool = new CarPool(Time.valueOf(req.queryParams("departure").concat(":2")), Time.valueOf(req.queryParams("arrival").concat(":2")), req.queryParams("origin"), Integer.parseInt(req.queryParams("quantity_days")), req.queryParams("destination"), Integer.parseInt(req.queryParams("quantity")), "Duncan", "ab@gmail.com", req.queryParams("notes"));
            wc.AddCarpool(carPool);

            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/main.handlebars"));
        });


        get("/join", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            return new HandlebarsTemplateEngine().render(new ModelAndView(map, "/views/joinleave.hbs"));
        });

    }


    private String LeaveCarPoolOpportunity(int id) {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;

        try {
            con = EstablishDbConnection();

            // Delete
            stmt = con.prepareStatement(MysqlScripts.DELETE);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            //update seat space
            stmt = con.prepareStatement(MysqlScripts.UPDATE);
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private String JoinCarPoolOpportunity(int id, String signInUser) {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rst = null;

        try {
            // Lookup
            con = EstablishDbConnection();
            stmt = con.prepareStatement(MysqlScripts.SEARCH_CARPOOL);
            stmt.setInt(1, id);
            rst = stmt.executeQuery();

            while (rst.next()) {
                if (rst.getInt("Available_seats") >= 1) {

                    //add carpool.
                    stmt = con.prepareStatement(MysqlScripts.INSERT_CARPOOL);

                    stmt.setTime(1, rst.getTime("Departure"));
                    stmt.setTime(2, rst.getTime("Expected_arrival"));
                    stmt.setString(3, rst.getString("Origin"));
                    stmt.setInt(4, rst.getInt("Days_available"));
                    stmt.setString(5, rst.getString("Destination"));
                    stmt.setInt(6, rst.getInt("Available_seats"));
                    stmt.setString(7, rst.getString("Leader"));
                    stmt.setString(8, signInUser);
                    stmt.setString(9, rst.getString("Email"));
                    stmt.setString(10, rst.getString("Notes"));
                    stmt.executeUpdate();

                    //update seat space
                    stmt = con.prepareStatement(MysqlScripts.UPDATE_SEAT);
                    stmt.setInt(1, id);
                    stmt.executeUpdate();

                } else {
                    System.out.println("No seat space left!");
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Successfully joined carpool opportunity";
    }
}


