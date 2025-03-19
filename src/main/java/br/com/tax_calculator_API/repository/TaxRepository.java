package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.TaxModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRepository extends JpaRepository<TaxModel, Long> {
    Optional<TaxModel> findByName(String name);
}
