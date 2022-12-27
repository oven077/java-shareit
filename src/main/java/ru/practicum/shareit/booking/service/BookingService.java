package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedState;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public BookingDto createBooking(BookingDto bookingDto, int userId) {

        Optional<Item> item;
        Boolean isAvailable;
        int itemId;

        item = itemRepository.findById(bookingDto.getItemId());

        if (item.isPresent()) {

            itemId = item.get().getId();
            isAvailable = item.get().getAvailable();


            if (!isAvailable) {
                throw new BadRequestException("Item is not available: " + itemId);
            }

            if (item.get().getOwner().getId() == userId) {
                throw new NotFoundException("Owner can not book self item");
            }


            if (!userRepository.findById(userId).isPresent()) {
                throw new NotFoundException("User not found: " + userId);
            }

            if (item.get().getOwner().getId() == userId) {
                throw new BadRequestException("Could not find user: " + userId);
            }

            if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
                throw new BadRequestException("End time has to be after start time");
            }

            if (bookingDto.getStart().isBefore((LocalDateTime.now())) ||
                    bookingDto.getEnd().isBefore((LocalDateTime.now()))) {
                throw new BadRequestException("Start time and end time has to be after current time");
            }

        } else {
            throw new NotFoundException("Not found item with id: " + bookingDto.getId());
        }

        bookingDto.setStatus(Status.WAITING);
        bookingDto.setBooker(userRepository.findById(userId).get());
        bookingDto.setItem(itemRepository.findById(itemId).get());

        return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(BookingMapper
                .INSTANCE.bookingDtoToBooking(bookingDto)));
    }

    public BookingDto approveBooking(int bookingId, Boolean approve, int userId) {

        Booking booking;

        if (bookingRepository.findById(bookingId).isPresent()) {

            booking = bookingRepository.findById(bookingId).get();

            if (booking.getItem().getOwner().getId() != userId) {
                throw new NotFoundException("only owner of item can make approve");
            }

            if (booking.getStatus() == Status.APPROVED) {
                throw new BadRequestException("double approve");
            }



            booking.setStatus(Status.APPROVED);

            return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(booking));

        } else {
            throw new NotFoundException("Not found booking with id: " + bookingId);
        }
    }

    public BookingDto getBooking(int bookingId, @Min(1) int userId) {
        Booking booking;

        if (bookingRepository.findById(bookingId).isPresent()) {
            booking = bookingRepository.findById(bookingId).get();
        } else {
            throw new NotFoundException("Not found booking with id: " + bookingId);
        }

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Bad user booking: " + userId);

        }


        return BookingMapper.INSTANCE.bookingToBookingDto(booking);


    }

    public Collection<BookingDto> getAllBookings(int userId) {

        if (userRepository.findById(userId).isPresent()) {

            return BookingMapper.INSTANCE
                    .sourceListToTargetList(bookingRepository.findAll().stream()
                            .filter(p -> p.getBooker().getId() == userId)
                            .collect(Collectors.toList()));

        } else {
            throw new NotFoundException("Not found user with id: " + userId);
        }


    }

    public Object getAll(int userId, String state) {

        List<Booking> result;
        User booker;


        if (userRepository.findById(userId).isPresent()) {
            booker = userRepository.findById(userId).get();
        } else {
            throw new NotFoundException("Not found booking with id: " + userId);
        }


        State bookingState = Objects.isNull(state) ? State.ALL : State.of(state);

        switch (bookingState) {
            case ALL:
                result = bookingRepository.findAllByBookerOrderByStartDesc(booker);
                break;
            case CURRENT:
                result = bookingRepository.findCurrentByBooker(booker, LocalDateTime.now());
                break;
            case PAST:
                result = bookingRepository.findPastByBooker(booker, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findFutureByBooker(booker, LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, State.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, State.REJECTED);
                break;

            case UNKNOWN:
            default:
                throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }


        return result.stream().
                map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.toList());
    }

    public Object getOwnerItemsAll(int userId, String state) {

        List<Booking> result;
        User owner;

        if (userRepository.findById(userId).isPresent()) {
            owner = userRepository.findById(userId).get();
        } else {
            throw new NotFoundException("Not found booking with id: " + userId);
        }


        State bookingState = Objects.isNull(state) ? State.ALL : State.of(state);

        switch (bookingState) {
            case ALL:
                result = bookingRepository.findAllByOwnerItems(owner);
                break;
            case CURRENT:
                result = bookingRepository.findCurrentByOwner(owner, LocalDateTime.now());
                break;
            case PAST:
                result = bookingRepository.findPastByOwner(owner, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findFutureByOwner(owner, LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, State.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, State.REJECTED);
                break;

            case UNKNOWN:
            default:
                throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }
        return result.stream().
                map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.toList());
    }

    public Object rejectBooking(int bookingId, Boolean approved, int userId) {
        Booking booking;

        if (bookingRepository.findById(bookingId).isPresent()) {

            booking = bookingRepository.findById(bookingId).get();

            if (booking.getItem().getOwner().getId() != userId) {
                throw new NotFoundException("only owner of item can make aprove");
            }

            booking.setStatus(Status.REJECTED);

            return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(booking));

        } else {
            throw new NotFoundException("Not found booking with id: " + bookingId);
        }
    }
}
