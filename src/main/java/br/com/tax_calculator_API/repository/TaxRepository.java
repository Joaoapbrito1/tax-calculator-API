package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.TaxModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaxRepository extends JpaRepository<TaxModel, Long> {
}
