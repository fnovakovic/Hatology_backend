package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.User;

import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.UserCrudRepository;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    private UserCrudRepository userCrudRepository;
    private UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository, UserCrudRepository userCrudRepository) {
        this.userRepository = userRepository;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(email);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+email+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), new ArrayList<>());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
