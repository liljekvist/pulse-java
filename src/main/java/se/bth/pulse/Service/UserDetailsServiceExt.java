package se.bth.pulse.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.bth.pulse.Repository.UserRepository;

@Service
public class UserDetailsServiceExt implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Create this repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
