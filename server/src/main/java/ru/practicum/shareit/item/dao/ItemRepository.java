package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.model.Booking;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


    @Query(value = "select * from items as i where i.id = ? and i.owner_id = ?", nativeQuery = true)
    Optional<Item> findByItemAndOwnerId(@Param("id") Integer itemId, @Param("owner_id") Integer ownerId);


    //не работает, просто для примера, со слов Яндекса = Артем должно работать
    @Query(value = "select b.start_date, b.booker_id, b.item_id, b.end_date, b.id, b.status, i.name\n" +
            "\n" +
            "FROM bookings b\n" +
            "         inner join items i on i.id = b.item_id\n" +
            "where i.owner_id = 4\n" +
            "order by b.start_date asc", nativeQuery = true)
    List<Booking> findLastBooking(@Param("id") Integer itemId, @Param("owner_id") Integer ownerId);


    @Query(value = "select b, b.booker.id as bookerId from Booking b where b.item.id = :id" +
            " and b.item.owner.id = :owner_id order by b.start asc ")
    List<Booking> findListBooking(@Param("id") Integer itemId, @Param("owner_id") Integer ownerId);


}
