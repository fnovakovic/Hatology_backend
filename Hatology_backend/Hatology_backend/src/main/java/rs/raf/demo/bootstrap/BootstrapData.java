package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BootstrapData implements CommandLineRunner {


    private final UserRepository userRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        User user3 = new User();
        user3.setEmail("fica@gmail.com");
        user3.setAdminPermission("1");
        user3.setPassword(Base64.getEncoder().encodeToString("123".getBytes(StandardCharsets.UTF_8)));
        userRepository.save(user3);

        System.out.println("Data loaded!");
    }
}
