package com.github.board.repository.auth.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @Column(name = "role_id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    @Column(name = "name",nullable = false)
    private String name;

}
