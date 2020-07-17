package com.excilys.cdb.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if ("user".equals(username)) {
            return new User("user",
                    "$2a$10$/clYcGWrinlVnDyD4UFiVu"
                            + "yDlxMWS3Y310WgrAVu35mu9Se3LsoKy",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException(
                    "User not found with username: " + username);
        }
    }

}
