package ru.savinov.bft_task.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.savinov.bft_task.entity.Customer;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.frontend.mapper.CustomerMapper;
import ru.savinov.bft_task.frontend.mapper.StatisticsRowMapper;
import ru.savinov.bft_task.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private StatisticsRowMapper statisticsRowMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;
    private List<Customer> customers;
    private List<CustomerDto> customerDtos;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("Иван")
                .lastName("Иванов")
                .age(30)
                .payment(new BigDecimal("25000.00"))
                .build();

        customerDto = CustomerDto.builder()
                .id(1L)
                .firstName("Иван")
                .lastName("Иванов")
                .age(30)
                .payment(new BigDecimal("25000.00"))
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .firstName("Петр")
                .lastName("Петров")
                .age(25)
                .payment(new BigDecimal("30000.00"))
                .build();

        CustomerDto customerDto2 = CustomerDto.builder()
                .id(2L)
                .firstName("Петр")
                .lastName("Петров")
                .age(25)
                .payment(new BigDecimal("30000.00"))
                .build();

        customers = Arrays.asList(customer, customer2);
        customerDtos = Arrays.asList(customerDto, customerDto2);
    }

    @Test
    void save_ShouldSaveCustomer_WhenValidDto() {
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.save(customerDto);

        verify(customerMapper).toEntity(customerDto);
        verify(customerRepository).save(customer);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException_WhenCustomerNotFound() {
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(customerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("не найден");

        verify(customerRepository).findById(customerId);
        verify(customerMapper, never()).toDto(any());
    }

}