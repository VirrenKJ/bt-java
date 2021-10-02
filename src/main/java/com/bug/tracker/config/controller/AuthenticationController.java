package com.bug.tracker.config.controller;

import com.bug.tracker.config.JwtUtil;
import com.bug.tracker.config.entity.AuthenticationRequest;
import com.bug.tracker.config.entity.AuthenticationResponse;
import com.bug.tracker.user.entity.UserBO;
import com.bug.tracker.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken (@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try{
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }catch(UsernameNotFoundException e){
            e.printStackTrace();
            throw new Exception("User not found");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        UserBO user = (UserBO) userDetails;
        user.setPassword(null);
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token, user));
    }

    @GetMapping("/current-user")
    public UserBO getCurrentUser(Principal principal){
        UserBO userBO = (UserBO) userDetailsService.loadUserByUsername((principal.getName()));
        userBO.setPassword(null);
        return userBO;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            e.printStackTrace();
            throw new Exception("USER DISABLED" + e.getMessage());
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
