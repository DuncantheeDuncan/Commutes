package co_mute.model;

import java.sql.Time;
import java.sql.Timestamp;

public class CarPool {

    private Timestamp Created_at;
    private Time Departure;
    private Time Expected_arrival;
    private String origin;
    private int Days_available;
    private String Destination;
    private int Available_seats;
    private String Leader;
    private String Email;
    private String Notes;

    public CarPool(Time departure, Time expected_arrival, String origin, int days_available, String destination, int available_seats, String leader, String email, String notes) {
        this.Departure = departure;
        this.Expected_arrival = expected_arrival;
        this.origin = origin;
        this.Days_available = days_available;
        this.Destination = destination;
        this.Available_seats = available_seats;
        this.Leader = leader;
        this.Email = email;
        this.Notes = notes;
    }


    public CarPool(Time departure, String origin, String destination, String leader, Timestamp Created_at) {
        this.Departure = departure;
        this.origin = origin;
        this.Destination = destination;
        this.Leader = leader;
        this.Created_at = Created_at;
    }

    public Timestamp getCreated_at() {
        return Created_at;
    }

    public Time getDeparture() {
        return Departure;
    }

    public Time getExpected_arrival() {
        return Expected_arrival;
    }

    public String getOrigin() {
        return origin;
    }

    public int getDays_available() {
        return Days_available;
    }

    public String getDestination() {
        return Destination;
    }

    public int getAvailable_seats() {
        return Available_seats;
    }

    public String getLeader() {
        return Leader;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNotes() {
        return Notes;
    }

}
