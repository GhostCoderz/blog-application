package com.ghostcoderz.blog_application.security;

import com.ghostcoderz.blog_application.entity.User;
import com.ghostcoderz.blog_application.exceptions.ResourceNotFoundException;
import com.ghostcoderz.blog_application.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Loading user from DB by username
        User user = this.userRepo.findByEmail(username).orElseThrow(() ->
            new ResourceNotFoundException("User ", " email ", username)
        );

        return user;
    }

}
