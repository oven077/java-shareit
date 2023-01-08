package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;


    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, @Min(1) int userId) {

        ItemRequest itemRequest;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        itemRequest = ItemRequestMapper.INSTANCE.itemRequestDtoToItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepository.findById(userId).get());
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.INSTANCE.itemRequestToItemRequestDto(itemRequestRepository.save(itemRequest));

    }

    //todo
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId, Pageable pageable) {

        List<ItemRequestDto> itemRequestDtoList;
        List<ItemDto> itemDtoList;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));


        itemRequestDtoList = ItemRequestMapper.INSTANCE.sourceListToTargetList(itemRequestRepository.findAll(pageable).toList());

        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {

            itemRequestDto.setItems(ItemMapper.INSTANCE
                    .sourceListToTargetList(itemRepository.findAll().stream()
                            .filter(p -> p.getRequest() != null)
                            .filter(p -> p.getRequest().getId() == itemRequestDto.getId())
                            .sorted((p1, p2) -> p1.getId() - p2.getId())
                            .collect(Collectors.toList())));
        }


        return itemRequestDtoList;


    }

    public List<ItemRequestDto> getItemRequestsAllOther(@Min(1) int userId, Pageable pageable) {

        List<ItemRequestDto> itemRequestDtoList;
        List<ItemDto> itemDtoList;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        itemRequestDtoList = ItemRequestMapper.INSTANCE.sourceListToTargetList(itemRequestRepository
                .findAllOtherItemRequests(userId, pageable));


        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {

            itemRequestDto.setItems(ItemMapper.INSTANCE
                    .sourceListToTargetList(itemRepository.findAll().stream()
                            .filter(p -> p.getRequest() != null)
                            .filter(p -> p.getRequest().getId() == itemRequestDto.getId())
                            .sorted((p1, p2) -> p1.getId() - p2.getId())
                            .collect(Collectors.toList())));
        }


        return itemRequestDtoList;
    }

    public ItemRequestDto getItemRequestById(int id, int userId) {


        ItemRequestDto itemRequestDto;

        itemRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("item request not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        itemRequestDto = ItemRequestMapper.INSTANCE.itemRequestToItemRequestDto(itemRequestRepository.findById(id).get());

        itemRequestDto.setItems(ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getRequest() != null)
                        .filter(p -> p.getRequest().getId() == itemRequestDto.getId())
                        .sorted((p1, p2) -> p1.getId() - p2.getId())
                        .collect(Collectors.toList())));

        return itemRequestDto;
    }
}
