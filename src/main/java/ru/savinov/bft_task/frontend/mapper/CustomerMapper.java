package ru.savinov.bft_task.frontend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.savinov.bft_task.frontend.dto.CustomerDto;
import ru.savinov.bft_task.entity.Customer;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    CustomerDto toDto(Customer entity);
    Customer toEntity(CustomerDto dto);
    List<CustomerDto> toDtoList(List<Customer> entities);

}
