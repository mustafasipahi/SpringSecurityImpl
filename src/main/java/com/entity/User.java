package com.entity;

import com.constant.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
