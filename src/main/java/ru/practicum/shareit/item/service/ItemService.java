package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    private final ItemRequestRepository itemRequestRepository;


    public ItemDto createItem(ItemDto itemDto, int userId) {
        Item newItem;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No User with this ID: " + userId));

        newItem = ItemMapper.INSTANCE.itemDtoToItem(itemDto);
        newItem.setOwner(userRepository.findById(userId).get());


        if (itemRequestRepository.findById(itemDto.getRequestId()).isPresent()) {
            newItem.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).get());
        }


        return ItemMapper.INSTANCE.itemToItemDto(itemRepository.save(newItem));
    }

    public ItemDto updateItem(ItemDto item, int itemId, int userId) {

        itemRepository.findByItemAndOwnerId(itemId, userId).orElseThrow(() ->
                new NotFoundException("No Item with this ID or wrong ID owner: " + userId));

        return ItemMapper.INSTANCE.itemToItemDto(itemRepository.save(ItemMapper.INSTANCE
                .updateItemFromDto(item, itemRepository.findById(itemId).get())));
    }


    public Collection<ItemDto> getAllItems(int userId) {


        Item item;
        ItemDto itemDto;
        List<Booking> listBooking;
        BookingDto bookingDto;
        Booking booking;
        Collection<ItemDto> itemDtoList;

        itemDtoList = ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getOwner().getId() == userId)
                        .sorted((p1, p2) -> p1.getId() - p2.getId())
                        .collect(Collectors.toList()));


        for (ItemDto dto : itemDtoList) {

            listBooking = itemRepository.findListBooking(dto.getId(), userId);


            if (!listBooking.isEmpty()) {

                booking = listBooking.get(0);

                bookingDto = BookingMapper.INSTANCE.bookingToBookingDto(booking);
                bookingDto.setBookerId(booking.getBooker().getId());

                dto.setLastBooking(bookingDto);
                if (listBooking.size() > 1) {
                    booking = listBooking.get(1);

                    bookingDto = BookingMapper.INSTANCE.bookingToBookingDto(booking);
                    bookingDto.setBookerId(booking.getBooker().getId());

                    dto.setNextBooking(bookingDto);
                }

            }
        }
        return itemDtoList;

    }


    public Collection<ItemDto> getAllItemsWithSearch(int userId, String text) {

        return ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getDescription().toLowerCase().contains(text.toLowerCase()) && p.getAvailable().equals(true))
                        .collect(Collectors.toList()));
    }

    public ItemDto getItemBookings(int itemId, int userId) {
        Item item;
        ItemDto itemDto;
        List<Booking> listBooking;
        List<Comment> listComment;
        BookingDto bookingDto;
        Booking booking;

        if (itemRepository.findById(itemId).isPresent()) {
            item = itemRepository.findById(itemId).get();
        } else {
            throw new NoSuchUserException("No Item with such ID: " + itemId);
        }

        itemDto = ItemMapper.INSTANCE.itemToItemDto(itemRepository.findById(itemId).get());

        listBooking = itemRepository.findListBooking(itemId, userId);
        listComment = commentRepository.findItemComments(itemId);


        if (!listBooking.isEmpty()) {
            booking = listBooking.get(0);

            bookingDto = BookingMapper.INSTANCE.bookingToBookingDto(booking);
            bookingDto.setBookerId(booking.getBooker().getId());

            itemDto.setLastBooking(bookingDto);

            if (listBooking.size() > 1) {
                booking = listBooking.get(1);

                bookingDto = BookingMapper.INSTANCE.bookingToBookingDto(booking);
                bookingDto.setBookerId(booking.getBooker().getId());

                itemDto.setNextBooking(bookingDto);
            }

        }
        itemDto.setComments(CommentMapper.INSTANCE.sourceListToTargetList(listComment));

        return itemDto;
    }


    public CommentDto addCommentByItemId(int itemId, int userId, CommentDto commentDto) {
        Item item;
        User user;
        Comment comment;
        if (itemRepository.findById(itemId).isPresent()) {
            item = itemRepository.findById(itemId).get();
        } else {
            throw new NotFoundException("No Item with this ID or wrong ID owner: " + userId);
        }

        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        } else {
            throw new NotFoundException("No Item with this ID or wrong ID owner: " + userId);
        }


        if (bookingRepository.findBookingWithUserAndItem(userId, itemId, Status.REJECTED, LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Do not have bookings with user: " + userId);
        }


        comment = CommentMapper.INSTANCE.commentDtoToComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);
        commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);
        commentDto.setAuthorName(user.getName());

        return commentDto;


    }
}
