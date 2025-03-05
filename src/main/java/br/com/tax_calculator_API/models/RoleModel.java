package br.com.tax_calculator_API.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public RoleModel(String name) {
        this.name = name;
    }

    public RoleModel() {
    }
}