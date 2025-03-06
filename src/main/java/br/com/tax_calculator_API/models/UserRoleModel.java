package br.com.tax_calculator_API.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "roles")
public class UserRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public UserRoleModel(String name) {
        this.name = name;
    }

    public UserRoleModel() {
    }
}