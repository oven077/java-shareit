package ru.practicum.shareit.booking.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

//@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class},
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    //    @Mapping(target = "itemId", ignore = true)
    BookingDto bookingToBookingDto(Booking booking);

    Booking bookingDtoToBooking(BookingDto bookingDto);

    List<BookingDto> sourceListToTargetList(List<Booking> sourceList);
}
