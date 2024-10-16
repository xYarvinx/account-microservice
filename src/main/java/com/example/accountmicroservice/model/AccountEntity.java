package com.example.accountmicroservice.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Set<Role> roles;
    private String fullName;

    @PrePersist
    @PreUpdate
    public void updateFullName() {
        this.fullName = this.firstName + " " + this.lastName;
    }

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;
}
