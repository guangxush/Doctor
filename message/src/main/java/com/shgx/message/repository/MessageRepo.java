package com.shgx.message.repository;

import com.shgx.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    Optional<List<Message>> findAllById(Long id);
}
