package ru.practicum.shareit.request.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {

    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestDto ItemRequestToItemRequestDto(ItemRequest itemRequest);

    ItemRequest ItemRequestDtoToItemRequest(ItemRequestDto ItemRequestDto);

    List<ItemRequestDto> sourceListToTargetList(List<ItemRequest> sourceList);

//    User updateUserFromDto(UserDto userDto, @MappingTarget User user);

}
