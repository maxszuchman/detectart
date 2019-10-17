package com.experta.detectart.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 100)
    @Column(unique = true)
    private String firstName;

    @NotEmpty
    @Size(max = 250)
    private String lastName;

    @NotEmpty
    @Size(max = 100)
    private String email;

    @NotEmpty
    @Size(max = 50)
    private String phone;

    @NotEmpty
    private String password;

    private String applicationToken;

    public User() {}

    public User(final Long id, @NotEmpty @Size(max = 100) final String firstName, @NotEmpty @Size(max = 250) final String lastName,
            @NotEmpty final String email, @NotEmpty final String phone, @NotEmpty final String password, final String applicationToken) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.applicationToken = applicationToken;
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(final String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public User copyInto(final User other) {

        other.setFirstName(this.firstName);
        other.setLastName(this.lastName);
        other.setEmail(this.email);
        other.setPhone(this.phone);
        other.setApplicationToken(this.applicationToken);

        return other;
    }
}
