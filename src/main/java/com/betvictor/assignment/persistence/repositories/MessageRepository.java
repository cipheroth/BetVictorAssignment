package com.betvictor.assignment.persistence.repositories;

import com.betvictor.assignment.persistence.entities.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageEntity, String> {

}
