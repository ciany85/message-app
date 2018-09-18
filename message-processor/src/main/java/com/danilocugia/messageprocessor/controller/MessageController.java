package com.danilocugia.messageprocessor.controller;

import com.danilocugia.messageprocessor.models.Message;
import com.danilocugia.messageprocessor.data.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
public class MessageController {

    @Autowired
    private DiscoveryClient discoveryClient;

    private String orderByProperty = "createdAt";

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> read(@RequestParam(value="last") Optional<Integer> last) {
        if (last.isPresent()) {
            return messageRepository.findAll(new PageRequest(0, last.get(), Sort.Direction.DESC, orderByProperty)).getContent();
        } else {
            return messageRepository.findAll(new Sort(Sort.Direction.DESC, orderByProperty));
        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public Message add(@RequestParam(value="message") String messageString) throws RestClientException, ServiceUnavailableException{
        String uuid;
        try {
            uuid = retrieveUuid();
        } catch (ServiceUnavailableException e) {
            uuid = "ServiceUnavailableException message-uuid-generator";
        }

        Message message = new Message();
        message.setMessage(messageString);
        message.setUuid(uuid);
        return messageRepository.save(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message edit(@RequestParam(value="id") Long id,
                        @RequestParam(value="message") String messageString) {
        Optional<Message> found = messageRepository.findById(id);
        if (found.isPresent()) {
            Message message = found.get();
            if (createdTimeInMilliseconds(message) > MILLISECONDS.convert(10, SECONDS)) {
                message.setMessage(messageString);
                return messageRepository.save(message);
            }
        }
        return null;
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void remove(@RequestParam(value="id") Long id) {
        Optional<Message> found = messageRepository.findById(id);
        if (found.isPresent()) {
            Message message = found.get();
            if (createdTimeInMilliseconds(message) > MILLISECONDS.convert(2, MINUTES)) {
                messageRepository.delete(message);
            }
        }
    }

    private String retrieveUuid() throws ServiceUnavailableException {
        RestTemplate restTemplate = new RestTemplate();

        URI service = serviceUrl()
                .map(s -> s.resolve("/message-uuid"))
                .orElseThrow(ServiceUnavailableException::new);

        return restTemplate.getForEntity(service, String.class).getBody();
    }

    private Optional<URI> serviceUrl() {
        return discoveryClient.getInstances("message-uuid-generator")
                .stream()
                .map(si -> si.getUri())
                .findFirst();
    }

    private long createdTimeInMilliseconds(Message message) {
        return System.currentTimeMillis() - message.getCreatedAt().getTime();
    }
}
