package com.daniel.falcon.interview.repository;

import com.daniel.falcon.interview.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    public List<Message> findByMessageText(String messageText);
}
