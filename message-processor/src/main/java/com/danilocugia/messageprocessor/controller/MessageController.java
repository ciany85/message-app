package com.danilocugia.messageprocessor.controller;

import com.danilocugia.messageprocessor.models.Message;
import com.danilocugia.messageprocessor.data.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    private String orderByProperty = "createdAt";

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> read(@RequestParam(value="last") Optional<Integer> last) {
        logger.info("GET request for " + (last.isPresent()? last.get() : "all") + " messages");
        if (last.isPresent()) {
            return messageRepository.findAll(new PageRequest(0, last.get(), Sort.Direction.DESC, orderByProperty)).getContent();
        } else {
            return messageRepository.findAll(new Sort(Sort.Direction.DESC, orderByProperty));
        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public Message add(@RequestParam(value="message") String messageString) throws RestClientException {
        logger.info("PUT request for message=" + messageString);
        String uuid;
        try {
            uuid = retrieveUuid();
        } catch (ServiceUnavailableException e) {
            uuid = "ServiceUnavailableException message-uuid-generator";
        }

        Message message = new Message();
        message.setMessage(messageString);
        message.setUuid(uuid);
        Message savedMessage = messageRepository.save(message);
        logger.info("Message saved with id " + savedMessage.getId());
        return savedMessage;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message edit(@RequestParam(value="id") Long id,
                        @RequestParam(value="message") String messageString) {
        logger.info("POST request form message id " + id);
        Optional<Message> found = messageRepository.findById(id);
        if (found.isPresent()) {
            Message message = found.get();
            message.setMessage(messageString);
            Message updatedMessage = messageRepository.save(message);
            logger.info("Message updated with message=" + messageString);
            return updatedMessage;
        }
        return null;
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void remove(@RequestParam(value="id") Long id) {
        logger.info("DELETE request for message id " + id);
        Optional<Message> found = messageRepository.findById(id);
        if (found.isPresent()) {
            Message message = found.get();
            messageRepository.delete(message);
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

}
