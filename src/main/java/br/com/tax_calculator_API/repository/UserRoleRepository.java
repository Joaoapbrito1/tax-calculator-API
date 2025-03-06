package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleModel,Long> {
}