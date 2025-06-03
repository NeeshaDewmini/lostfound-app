package com.lostfound.lostfound.repositories;

import com.lostfound.lostfound.model.Request;
import com.lostfound.lostfound.model.User;
import com.lostfound.lostfound.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUser(User user);
    boolean existsByUserAndItem(User user, Item item);
    Optional<Request> findByUserAndItem(User user, Item item);


}