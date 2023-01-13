package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, int userId) {

        ItemRequest itemRequest;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        itemRequest = ItemRequestMapper.INSTANCE.itemRequestDtoToItemRequest(itemRequestDto);
//        itemRequest.setRequestor(userRepository.findById(userId).get());
        itemRequest.setRequestor(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found")));
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.INSTANCE.itemRequestToItemRequestDto(itemRequestRepository.save(itemRequest));

    }

    //todo
    public List<ItemRequestDto> getItemRequests(int userId, int page, int pageSize) {

        List<ItemRequestDto> itemRequestDtoList;

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        itemRequestDtoList = ItemRequestMapper.INSTANCE
                .sourceListToTargetList(itemRequestRepository.findAll());


        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {

            itemRequestDto.setItems(ItemMapper.INSTANCE
                    .sourceListToTargetList(itemRepository.findAll().stream()
                            .filter(p -> p.getRequest() != null)
                            .filter(p -> p.getRequest().getId() == itemRequestDto.getId())
                            .sorted(Comparator.comparingInt(Item::getId))
                            .collect(Collectors.toList())));
        }
        return itemRequestDtoList;
    }

    public List<ItemRequestDto> getItemRequestsAllOther(int userId, int page, int pageSize) {

        List<ItemRequestDto> itemRequestDtoList;
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final Pageable pageable = PageRequest.of(page, pageSize, sort);

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        itemRequestDtoList = ItemRequestMapper.INSTANCE.sourceListToTargetList(itemRequestRepository
                .findAllOtherItemRequests(userId, pageable));


        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {

            itemRequestDto.setItems(ItemMapper.INSTANCE
                    .sourceListToTargetList(itemRepository.findAll().stream()
                            .filter(p -> p.getRequest() != null)
                            .filter(p -> p.getRequest().getId() == itemRequestDto.getId())
                            .sorted(Comparator.comparingInt(Item::getId))
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
                        .sorted(Comparator.comparingInt(Item::getId))
                        .collect(Collectors.toList())));

        return itemRequestDto;
    }
}
