package ru.savinov.bft_task.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.savinov.bft_task.entity.Customer;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.frontend.dto.CustomerStatisticsDto;
import ru.savinov.bft_task.frontend.mapper.CustomerMapper;
import ru.savinov.bft_task.frontend.mapper.StatisticsRowMapper;
import ru.savinov.bft_task.repository.CustomerRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    JdbcTemplate jdbcTemplate;
    StatisticsRowMapper statisticsRowMapper;

    @Transactional
    public void save(CustomerDto customerDto) {
        log.info("Сохранение [customer]: {}", customerDto);
        Customer toSave = customerMapper.toEntity(customerDto);
        Customer saved = customerRepository.save(toSave);
        log.info("[customer] успешно сохранен с Id: {}", saved.getId());
    }

    @Transactional
    public void delete(CustomerDto customerDto) {
        log.info("Удаление [customer] по Id: {}", customerDto);
        Long customerId = customerDto.getId();
        customerRepository.deleteById(customerId);
    }

    @Transactional(readOnly = true)
    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("[customer] с id:{} не найден " + id));
        return customerMapper.toDto(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDtoList(customers);
    }


    @Transactional(readOnly = true)
    public List<CustomerDto> findByFilter(String lastname, Integer age) {
        List<Customer> customersByName = customerRepository.findByLastNameAndAge(lastname, age);
        return customerMapper.toDtoList(customersByName);
    }

    public CustomerStatisticsDto getStatistics() {
        String sql = """
            SELECT 
                AVG(age) AS avg_age,
                MIN(age) AS min_age,
                MAX(age) AS max_age,
     
                AVG(payment) AS avg_payment,
                MIN(payment) AS min_payment,
                MAX(payment) AS max_payment,
                
                SUM(payment) AS total_payment,
                COUNT(*) AS total_customers
            FROM customer
            """;
        log.debug("Выполнение запроса статистики");
        return jdbcTemplate.queryForObject(sql, statisticsRowMapper);
    }

}
