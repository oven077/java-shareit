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


        itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Not found item with id: " + bookingDto.getId()));

        item = itemRepository.findById(bookingDto.getItemId());

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

        bookingDto.setStatus(Status.WAITING);
        bookingDto.setBooker(userRepository.findById(userId).get());
        bookingDto.setItem(itemRepository.findById(itemId).get());

        return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(BookingMapper
                .INSTANCE.bookingDtoToBooking(bookingDto)));
    }

    public BookingDto setApproveOrRejectBooking(int bookingId, Boolean approve, int userId) {

        Booking booking;

        bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Not found booking with id: " + bookingId));

        booking = bookingRepository.findById(bookingId).get();

        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("only owner of item can make approve");
        }

        if (booking.getStatus() == Status.APPROVED) {
            throw new BadRequestException("double approve");
        }

        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(booking));

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

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));

        return BookingMapper.INSTANCE
                .sourceListToTargetList(bookingRepository.findAll().stream()
                        .filter(p -> p.getBooker().getId() == userId)
                        .collect(Collectors.toList()));
    }

    public List<BookingDto> getAll(int userId, String state) {

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
                result = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, Status.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, Status.REJECTED);
                break;

            case UNKNOWN:
            default:
                throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }


        return result.stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getOwnerItemsAll(int userId, String state) {

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
                result = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, Status.WAITING, LocalDateTime.now());
                break;
            case REJECTED:
                result = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(owner, Status.REJECTED, LocalDateTime.now());
                break;

            case UNKNOWN:
            default:
                throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        return result.stream()
                .map(BookingMapper.INSTANCE::bookingToBookingDto)
                .collect(Collectors.toList());
    }
}
