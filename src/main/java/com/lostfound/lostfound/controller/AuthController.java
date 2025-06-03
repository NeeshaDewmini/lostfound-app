package com.lostfound.lostfound.controller;

import com.lostfound.lostfound.dto.*;
import com.lostfound.lostfound.model.User;
import com.lostfound.lostfound.model.User.Role;
import com.lostfound.lostfound.repositories.UserRepository;
import com.lostfound.lostfound.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        userRepo.save(user);
        return "User registered";
    }

    @PostMapping("/signin")
    public JwtResponse signin(@RequestBody LoginRequest request) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    final String token = jwtUtil.generateToken(userDetails);
    return new JwtResponse(token);
}



}
