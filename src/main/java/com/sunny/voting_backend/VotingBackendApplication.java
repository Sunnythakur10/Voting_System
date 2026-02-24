package com.sunny.voting_backend;



import com.sunny.voting_backend.model.ElectionState;
import com.sunny.voting_backend.model.User;
import com.sunny.voting_backend.repository.ElectionStateRepository;
import com.sunny.voting_backend.repository.UserRepository;
import com.sunny.voting_backend.service.UserService;
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
			// Check if the Super Admin exists
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN"); // <--- CRITICAL: Give them the keys to the kingdom

				userRepository.save(admin);
				System.out.println("✅ Super Admin Bootstrapped: admin / admin123");
			}
		};
	}

	@Bean
	CommandLineRunner run(UserService userService, ElectionStateRepository electionStateRepository) {
		return args -> {
			// ... your existing admin creation code is here ...

			// --- ADD THIS MISSING SEED LOGIC ---
			if (!electionStateRepository.existsById(1L)) {
				ElectionState defaultState = new ElectionState();
				defaultState.setId(1L);
				// Leave times null for now, the Admin will configure them via Postman
				electionStateRepository.save(defaultState);
				System.out.println("Election State initialized in database.");
			}
		};
	}
}

