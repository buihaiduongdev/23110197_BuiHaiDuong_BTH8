package com.example.bth07.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.bth07.entity.Category;
import com.example.bth07.entity.User;
import com.example.bth07.entity.User.Role;
import com.example.bth07.repository.CategoryRepository;
import com.example.bth07.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepo;
	private final CategoryRepository categoryRepo;
	private final PasswordEncoder passwordEncoder;

	public DataLoader(UserRepository userRepo, CategoryRepository categoryRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.categoryRepo = categoryRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		// Insert Users nếu chưa có
		if (userRepo.count() == 0) {
			User admin = User.builder().username("admin").password(passwordEncoder.encode("123456"))
					.fullName("Nguyen Van Admin").email("admin@mail.com").phone("0123456789").role(Role.admin).build();

			User manager = User.builder().username("manager").password(passwordEncoder.encode("123456"))
					.fullName("Nguyen Van Manager").email("manager@mail.com").phone("0987654321").role(Role.manager)
					.build();

			User user = User.builder().username("user").password(passwordEncoder.encode("123456"))
					.fullName("Nguyen Van User").email("user@mail.com").phone("0111222333").role(Role.user).build();

			userRepo.save(admin);
			userRepo.save(manager);
			userRepo.save(user);

			// Insert Categories
			if (categoryRepo.count() == 0) {
				categoryRepo.save(Category.builder().name("Electronics").description("Electronic items").image(null)
						.createdBy(admin).build());

				categoryRepo.save(Category.builder().name("Books").description("Books and magazines").image(null)
						.createdBy(admin).build());

				categoryRepo.save(Category.builder().name("Clothes").description("Men and Women Clothes").image(null)
						.createdBy(admin).build());
			}
		}
	}
}
