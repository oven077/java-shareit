package ru.practicum.shareit.item.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "request.id", target = "requestId")

    ItemDto itemToItemDto(Item item);

    Item itemDtoToItem(ItemDto itemDto);

    List<ItemDto> sourceListToTargetList(List<Item> sourceList);


    @Mapping(target = "id", ignore = true)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);


}
