package org.acme.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_blocking")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Or GenerationType.AUTO if you want the database to handle it
    private Long id;



    @Column(unique = true, nullable = false)
    private String email;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(Long id, String email) {
        this.id = id;

        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
