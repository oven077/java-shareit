package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedState;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking() {

        Item item = new Item();
        User owner = new User();
        User user = new User();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.save(any())).thenReturn(bookingToSave);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Booking actualBooking = BookingMapper.INSTANCE.bookingDtoToBooking(bookingService.createBooking(BookingMapper
                .INSTANCE.bookingToBookingDto(bookingToSave), userId));

        assertEquals(bookingToSave, actualBooking);
        Mockito.verify(bookingRepository).save(any());

    }


    @Test
    void createBooking_NotAvailable() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        BookingDto bookingDto = BookingDto.builder().build();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);

        item.setAvailable(false);
        item.setOwner(owner);
        item.setId(itemId);

        bookingToSave.setItem(item);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            bookingService.createBooking(bookingDto, userId);
        });

        assertEquals("Item is not available: " + itemId, thrown.getMessage());
    }

    @Test
    void createBooking_OwnerBookSelfItem() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        BookingDto bookingDto = BookingDto.builder().build();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        owner.setId(userId);
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);
        item.setId(itemId);

        bookingToSave.setItem(item);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.createBooking(bookingDto, userId);
        });

        assertEquals("Owner can not book self item", thrown.getMessage());
    }

    @Test
    void createBooking_UserNotFound() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        BookingDto bookingDto = BookingDto.builder().build();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);
        item.setId(itemId);

        bookingToSave.setItem(item);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.createBooking(bookingDto, userId);
        });
        assertEquals("User not found: " + userId, thrown.getMessage());
    }

    @Test
    void createBooking_EndBeforeStart() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        BookingDto bookingDto = BookingDto.builder().build();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        Booking bookingToSave = new Booking();

        bookingDto.setStart(LocalDateTime.now().plusSeconds(120));
        bookingDto.setEnd(LocalDateTime.now().minusSeconds(120));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);
        item.setId(itemId);

        bookingToSave.setItem(item);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            bookingService.createBooking(bookingDto, userId);
        });
        assertEquals("End time has to be after start time", thrown.getMessage());
    }

    @Test
    void createBooking_StartEndTimeAfterCurrent() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        BookingDto bookingDto = BookingDto.builder().build();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        Booking bookingToSave = new Booking();

        bookingDto.setStart(LocalDateTime.now().minusSeconds(120));
        bookingDto.setEnd(LocalDateTime.now().minusSeconds(80));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);
        item.setId(itemId);

        bookingToSave.setItem(item);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            bookingService.createBooking(bookingDto, userId);
        });
        assertEquals("Start time and end time has to be after current time", thrown.getMessage());
    }


    @Test
    void setApproveOrRejectBooking() {

        Item item = new Item();
        User owner = new User();
        User user = new User();

        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.save(any())).thenReturn(bookingToSave);
        Mockito.when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(bookingToSave));

        Booking actualBooking = BookingMapper.INSTANCE.bookingDtoToBooking(bookingService
                .setApproveOrRejectBooking(bookingId, true, userId));

        assertEquals(bookingToSave, actualBooking);
        Mockito.verify(bookingRepository).save(any());


    }

    @Test
    void getBooking() {

        Item item = new Item();
        User owner = new User();
        User user = new User();


        owner.setId(12);
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingToSave));

        Booking actualBooking = BookingMapper.INSTANCE.bookingDtoToBooking(bookingService.getBooking(bookingId, userId));

        assertEquals(bookingToSave, actualBooking);
    }

    @Test
    void getAllBookings() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        bookingListExpected.add(bookingToSave);


        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAll()).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAllBookings(userId);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByBookerOrderByStartDesc(user, pageable)).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "ALL", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);
    }

    @Test
    void getAll_current() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findCurrentByBooker(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "CURRENT", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll_past() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().minusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().minusSeconds(40));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findPastByBooker(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "PAST", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll_future() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findFutureByBooker(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "FUTURE", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll_waiting() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "WAITING", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll_rejected() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();


        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);


        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByBookerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getAll(userId, "REJECTED", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getAll_unknown() {

        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();


        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);


        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UnsupportedState thrown = assertThrows(UnsupportedState.class, () -> {
            bookingService.getAll(userId, "UNKNOWN", 0, 10);
        });

        assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown.getMessage());

    }

    @Test
    void getOwnerItemsAll() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByOwnerItems(user, pageable)).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "ALL", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);
    }

    @Test
    void getOwnerItems_current() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findCurrentByOwner(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "CURRENT", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);


    }

    @Test
    void getOwnerItems_past() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().minusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().minusSeconds(40));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findPastByOwner(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "PAST", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getOwnerItems_future() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);
        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findFutureByOwner(any(User.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "FUTURE", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getOwnerItems_waiting() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);

        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "WAITING", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);


    }

    @Test
    void getOwnerItems_rejected() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();


        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);


        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookingListExpected);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<BookingDto> actualBookingList = bookingService.getOwnerItemsAll(userId, "REJECTED", 0, 10);

        assertEquals(BookingMapper.INSTANCE.sourceListToTargetList(bookingListExpected), actualBookingList);

    }

    @Test
    void getOwnerItems_unknown() {
        Item item = new Item();
        User owner = new User();
        User user = new User();
        List<Booking> bookingListExpected = new ArrayList<>();
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;
        Booking bookingToSave = new Booking();


        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        bookingListExpected.add(bookingToSave);


        owner.setId(12);
        bookingToSave.setStart(LocalDateTime.now().plusSeconds(120));
        bookingToSave.setEnd(LocalDateTime.now().plusSeconds(240));
        bookingToSave.setId(1);
        bookingToSave.setBooker(user);

        item.setAvailable(true);
        item.setOwner(owner);

        bookingToSave.setItem(item);

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UnsupportedState thrown = assertThrows(UnsupportedState.class, () -> {
            bookingService.getOwnerItemsAll(userId, "UNKNOWN", 0, 10);
        });

        assertEquals("Unknown state: UNSUPPORTED_STATUS", thrown.getMessage());


    }


}