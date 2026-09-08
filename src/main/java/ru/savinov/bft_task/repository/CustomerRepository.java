package ru.savinov.bft_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savinov.bft_task.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
