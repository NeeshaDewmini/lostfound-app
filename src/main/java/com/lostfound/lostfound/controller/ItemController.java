package com.lostfound.lostfound.controller;

import com.lostfound.lostfound.model.Item;
import com.lostfound.lostfound.model.User;
import com.lostfound.lostfound.repositories.UserRepository;
import com.lostfound.lostfound.security.JwtUtil;
import com.lostfound.lostfound.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // CREATE item (linked with logged-in user)
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody Item item, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Optional<User> userOpt = userRepo.findByUsername(username);
            if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid user in token");

            item.setUser(userOpt.get());
            return ResponseEntity.ok(itemService.saveItem(item));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving item: " + e.getMessage());
        }
    }

    // GET all items (ADMIN and STAFF only)
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(@RequestHeader("Authorization") String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.substring(7));
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(403).build();

        User user = userOpt.get();
        if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF) {
            return ResponseEntity.ok(itemService.getAllItems());
        } else {
            return ResponseEntity.ok(itemService.findByUser(user));
        }
    }

    // GET item by ID (only if owner or ADMIN/STAFF)
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        Optional<Item> itemOpt = itemService.getItemById(id);
        if (itemOpt.isEmpty()) return ResponseEntity.notFound().build();

        String username = jwtUtil.extractUsername(authHeader.substring(7));
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(403).build();

        User user = userOpt.get();
        Item item = itemOpt.get();
        if (item.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.status(403).body("You are not authorized to view this item.");
        }
    }

    // UPDATE item (only if owner or ADMIN/STAFF)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item updatedItem, @RequestHeader("Authorization") String authHeader) {
        Optional<Item> itemOpt = itemService.getItemById(id);
        if (itemOpt.isEmpty()) return ResponseEntity.notFound().build();

        String username = jwtUtil.extractUsername(authHeader.substring(7));
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(403).build();

        User user = userOpt.get();
        Item item = itemOpt.get();

        if (item.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF) {
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setStatus(updatedItem.getStatus());
            return ResponseEntity.ok(itemService.saveItem(item));
        } else {
            return ResponseEntity.status(403).body("Not authorized to update this item.");
        }
    }

    // DELETE item (only if owner or ADMIN/STAFF)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        Optional<Item> itemOpt = itemService.getItemById(id);
        if (itemOpt.isEmpty()) return ResponseEntity.notFound().build();

        String username = jwtUtil.extractUsername(authHeader.substring(7));
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(403).build();

        User user = userOpt.get();
        Item item = itemOpt.get();

        if (item.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF) {
            itemService.deleteItem(id);
            return ResponseEntity.ok("Item deleted");
        } else {
            return ResponseEntity.status(403).body("Not authorized to delete this item.");
        }
    }
}
