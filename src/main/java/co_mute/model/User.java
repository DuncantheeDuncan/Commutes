package co_mute.model;

import co_mute.worker.Worker;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class User {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;

    public User(String name, String surname, String email, String phone, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = Worker.Password(password);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.password = password;
    }
}
