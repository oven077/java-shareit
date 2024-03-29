package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {


    @Query(value = "select i from ItemRequest i where i.requestor.id <> :id")
    List<ItemRequest> findAllOtherItemRequests(@Param("id") Integer itemId, Pageable pageable);


}
