package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel,Long> {
}