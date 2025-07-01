/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.util.Date;

public class customer implements Serializable{
    private int id;
    private String email;
    private String password;
    private String fullName;
    private String role ;
    private String facebookId;
    private String googleId;
    private String address;
    private Date createdAt;

    public customer() {}

    // Constructor for registration
    public customer(String email, String password, String fullName, String address) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.createdAt = new Date();
    }

    public customer(int id, String email, String password, String fullName, String role, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.address = address;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFacebookId() { return facebookId; }
    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }

    public String getGoogleId() { return googleId; }
    public void setGoogleId(String googleId) { this.googleId = googleId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}