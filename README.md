# Calculator_API

Application for managing tax calculations in Brazil, in which you can register users with the User or Admin role, allowing personalized access to each endpoint, and most importantly, register taxes and perform calculations.

##  Project Structure

The project is organized as follows:

```
├── src/main/java/br/com/tax_calculator_API
│   ├── controllers      
│   ├── dtos             
│   ├── enums             
│   ├── exceptions            
│   ├── infra           
│   ├── models     
│   ├── repository         
│   ├── services       
│   ├── TaxCalculatorApiApplication.java  # Main application class
└── ...
```
##  Technologies Used

- **Java 21**
- **Spring Boot**
- **Spring Security**
- **JPA/Hibernate**
- **PostgreSQL**
- **Maven**
- **Swagger**

##  How to Run the Project

Follow the steps below to run the application locally:

1. **Clone the repository**:

   ```bash
   git clone git@github.com:Joaoapbrito1/tax-calculator-API.git
   ```


2. **Build and run the application**:

   ```bash
   mvn spring-boot:run
   ```

3. **Access the API**:

    - The application will be available at: [http://localhost:8080](http://localhost:8080)
    - Swagger documentation will be available at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   

