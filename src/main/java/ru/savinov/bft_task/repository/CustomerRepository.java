package ru.savinov.bft_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.savinov.bft_task.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = """
            SELECT * FROM customer 
            WHERE (:lastName IS NULL OR LOWER(last_name) LIKE LOWER(CONCAT('%', :lastName, '%')))
            AND (:age IS NULL OR age = :age)
            ORDER BY last_name
            """, nativeQuery = true)
    List<Customer> findByLastNameAndAge(
            @Param("lastName") String lastName,
            @Param("age") Integer age
    );

}
