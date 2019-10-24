package com.experta.detectart.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends AuditModel {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @NotEmpty
    @Size(max = 100)
    private String id;

    @NotEmpty
    @Size(max = 250)
    private String fullName;

    private String applicationToken;

    public User() {}

    public User(@NotEmpty @Size(max = 250) final String fullName,
            @NotEmpty @Size(max = 100) final String email, final String applicationToken) {
        super();
        this.fullName = fullName;
        this.id = email;
        this.applicationToken = applicationToken;
    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(final Long id) {
//        this.id = id;
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String lastName) {
        this.fullName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(final String email) {
        this.id = email;
    }

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(final String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public User copyInto(final User other) {

        other.setFullName(this.fullName);
        other.setId(this.id);
        other.setApplicationToken(this.applicationToken);

        return other;
    }
}
