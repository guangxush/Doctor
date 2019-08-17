package com.shgx.pingback.repository;

import com.shgx.pingback.model.PingBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@Repository
public interface PingBackRepo extends JpaRepository<PingBack, Long> {

    Optional<List<PingBack>> findAllById(Long id);
}
