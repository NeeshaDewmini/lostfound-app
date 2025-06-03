package com.lostfound.lostfound.service;

import com.lostfound.lostfound.model.Item;
import com.lostfound.lostfound.model.User;
import com.lostfound.lostfound.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepo;

    // Save item
    public Item saveItem(Item item) {
        return itemRepo.save(item);
    }

    // Get all items
    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    // Get item by ID
    public Optional<Item> getItemById(Long id) {
        return itemRepo.findById(id);
    }

    // Delete item by ID
    public void deleteItem(Long id) {
        itemRepo.deleteById(id);
    }

    public List<Item> findByStatus(Item.Status status) {
    return itemRepo.findByStatus(status);
}

public List<Item> findByUser(User user) {
    return itemRepo.findByUser(user);
}

}
