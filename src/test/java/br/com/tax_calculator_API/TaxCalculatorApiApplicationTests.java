package br.com.tax_calculator_API;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TaxCalculatorApiApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testMain() {
        TaxCalculatorApiApplication.main(new String[] {});
    }

}
