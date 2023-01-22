package ru.practicum.shareit.request.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {

    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);

    ItemRequest itemRequestDtoToItemRequest(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> sourceListToTargetList(List<ItemRequest> sourceList);

    List<ItemRequest> targetListToSourceList(List<ItemRequestDto> sourceList);


}
