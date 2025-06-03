package com.lostfound.lostfound.controller;

import com.lostfound.lostfound.model.Request;
import com.lostfound.lostfound.model.User;
import com.lostfound.lostfound.model.Item;
import com.lostfound.lostfound.dto.RequestDTO;
import com.lostfound.lostfound.repositories.RequestRepository;
import com.lostfound.lostfound.repositories.UserRepository;
import com.lostfound.lostfound.repositories.ItemRepository;
import com.lostfound.lostfound.service.RequestService;
import com.lostfound.lostfound.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // Create a new request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RequestDTO dto) {
    Optional<User> userOpt = userRepo.findById(dto.getUserId());
    Optional<Item> itemOpt = itemRepo.findById(dto.getItemId());

    if (userOpt.isEmpty() || itemOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Invalid user or item ID.");
    }

    User user = userOpt.get();
    Item item = itemOpt.get();

    if (requestRepo.existsByUserAndItem(user, item)) {
        return ResponseEntity.badRequest().body("You already requested this item.");
    }

    Request request = new Request();
    request.setUser(user);
    request.setItem(item);
    request.setStatus(Request.Status.PENDING);

    return ResponseEntity.ok(requestRepo.save(request));
}


    
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    //  User: Get their own requests
    @GetMapping("/my")
    public ResponseEntity<List<Request>> getMyRequests(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String username = jwtUtil.extractUsername(token);
            Optional<User> userOpt = userRepo.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Request> userRequests = requestRepo.findByUser(userOpt.get());
            return ResponseEntity.ok(userRequests);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    //  Get a request by ID
    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  Admin: Update request status
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Request> requestOptional = requestRepo.findById(id);
        if (requestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Request request = requestOptional.get();
        try {
            request.setStatus(Request.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value. Use PENDING, APPROVED, or REJECTED.");
        }

        requestRepo.save(request);
        return ResponseEntity.ok("Request status updated to " + status.toUpperCase());
    }

    //  Delete request 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
    Optional<Request> requestOpt = requestRepo.findById(id);
    if (requestOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // Extract logged-in user
    String token = authHeader.substring(7);
    String username = jwtUtil.extractUsername(token);
    Optional<User> userOpt = userRepo.findByUsername(username);

    if (userOpt.isEmpty()) {
        return ResponseEntity.status(403).body("Unauthorized user.");
    }

    User loggedInUser = userOpt.get();
    Request request = requestOpt.get();

    
    boolean isAdminOrStaff = loggedInUser.getRole() == User.Role.ADMIN || loggedInUser.getRole() == User.Role.STAFF;
    boolean isOwner = request.getUser().getId().equals(loggedInUser.getId());

    if (isAdminOrStaff || isOwner) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok().body("Request deleted.");
    } else {
        return ResponseEntity.status(403).body("You are not authorized to delete this request.");
    }
}

}
