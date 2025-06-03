package com.lostfound.lostfound.repositories;

import com.lostfound.lostfound.model.Item;
import com.lostfound.lostfound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(Item.Status status);
    List<Item> findByUser(User user);

}