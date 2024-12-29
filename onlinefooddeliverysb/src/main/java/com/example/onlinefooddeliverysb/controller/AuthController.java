package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.config.JwtProvider;
import com.example.onlinefooddeliverysb.model.USER_ROLE;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.repository.UserRepository;
import com.example.onlinefooddeliverysb.request.LoginRequest;
import com.example.onlinefooddeliverysb.response.AuthResponse;
import com.example.onlinefooddeliverysb.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody User user
    ) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if (isEmailExist != null) {
            throw new Exception("Email is already used with another account");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Password and Confirm Password must match");
        }

        if (user.getPassword().length() < 8) {
            throw new Exception("Password must be at least 8 characters");
        }

        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFirstName(user.getFirstName());
        createdUser.setLastName(user.getLastName());
        createdUser.setConfirmPassword(user.getConfirmPassword());
        createdUser.setLocationName(user.getLocationName());
        createdUser.setLocationDescription(user.getLocationDescription());
        createdUser.setRole(user.getRole());
        createdUser.setPhoneNumber(user.getPhoneNumber());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success");
        authResponse.setRole(savedUser.getRole());
        authResponse.setId(savedUser.getId());
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setPhoneNumber(user.getPhoneNumber());
        authResponse.setLocationName(user.getLocationName());
        authResponse.setLocationDescription(user.getLocationDescription());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest req){

        String username=req.getEmail();
        String password=req.getPassword();

        Authentication authentication=authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt= jwtProvider.generateToken(authentication);

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(username));
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("login success");
        authResponse.setRole(USER_ROLE.valueOf(role));
        authResponse.setId(user.getId());
        authResponse.setFirstName(user.getFirstName());
        authResponse.setLastName(user.getLastName());
        authResponse.setPhoneNumber(user.getPhoneNumber());
        authResponse.setLocationName(user.getLocationName());
        authResponse.setLocationDescription(user.getLocationDescription());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails=customerUserDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
