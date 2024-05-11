package edu.school21.sockets.models;

public class User {
    private Long id;
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String email) {
        this.email = email;
    }
    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }
    public User() {}
    @Override
    public String toString() {
        return "ID = " + id + " ---- Email = " + email + " ---- Password =  "+ password + "\n";
    }
}
