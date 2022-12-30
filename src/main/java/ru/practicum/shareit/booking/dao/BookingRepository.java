package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and :now between b.start and b.end order by b.start desc ")
    List<Booking> findCurrentByBooker(@Param("booker") User booker, @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and b.end < :now order by b.start desc ")
    List<Booking> findPastByBooker(@Param("booker") User booker, @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.booker = :booker and b.start > :now order by b.start desc ")
    List<Booking> findFutureByBooker(@Param("booker") User booker, @Param("now") LocalDateTime now);


    List<Booking> findAllByBookerAndStatusOrderByStartDesc(@Param("booker") User booker,
                                                           @Param("status") Status status);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner order by b.start desc ")
    List<Booking> findAllByOwnerItems(@Param("owner") User owner);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner and :now between b.start and b.end order by b.start desc ")
    List<Booking> findCurrentByOwner(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner and b.status = :status and b.start > :now order by b.start desc ")
    List<Booking> findAllByOwnerAndStatusOrderByStartDesc(@Param("owner") User owner, @Param("status") Status status,
                                                          @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner and b.end < :now order by b.start desc ")
    List<Booking> findPastByOwner(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.item.owner = :owner and b.start > :now order by b.start desc ")
    List<Booking> findFutureByOwner(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query(value = "select b from Booking b " +
            "where b.item.id = :itemId and b.booker.id = :userId and b.status <> :status and b.start < :now order by b.start desc ")
    List<Booking> findBookingWithUserAndItem(@Param("userId") int userId, @Param("itemId") int itemId, @Param("status") Status status,
                                             @Param("now") LocalDateTime now);


}

