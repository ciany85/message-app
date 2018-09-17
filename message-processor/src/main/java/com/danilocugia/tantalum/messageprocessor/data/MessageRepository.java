package com.danilocugia.tantalum.messageprocessor.data;

import com.danilocugia.tantalum.messageprocessor.models.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    List<Message> findAll(Sort sort);

}
