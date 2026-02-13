package com.sunny.voting_backend;



import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VotingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingBackendApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			// Check if user exists so we don't create duplicates
			if (userRepository.findByUsername("sunny").isEmpty()) {
				User user = new User();
				user.setUsername("sunny");
				// CRITICAL: We encode the password! "12345" becomes "$2a$10$..."
				user.setPassword(passwordEncoder.encode("12345"));
				user.setRole("ROLE_VOTER");

				userRepository.save(user);
				System.out.println("✅ Test User Created: sunny / 12345");
			}
		};
	}
}

