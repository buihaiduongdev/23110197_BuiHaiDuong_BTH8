package com.example.bth07.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bth07.entity.Category;
import com.example.bth07.entity.Product;
import com.example.bth07.entity.User;
import com.example.bth07.entity.User.Role;
import com.example.bth07.repository.CategoryRepository;
import com.example.bth07.repository.ProductRepository;
import com.example.bth07.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

	private final UserRepository userRepo;
	private final CategoryRepository categoryRepo;
	private final ProductRepository productRepo;

	public DataLoader(UserRepository userRepo, CategoryRepository categoryRepo, ProductRepository productRepo) {
		this.userRepo = userRepo;
		this.categoryRepo = categoryRepo;
		this.productRepo = productRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		// Insert Users if not exists
		if (userRepo.count() == 0) {
			User admin = User.builder().username("admin").password("123456").fullName("Nguyen Van Admin")
					.email("admin@mail.com").phone("0123456789").role(Role.admin).build();

			User manager = User.builder().username("manager").password("123456").fullName("Nguyen Van Manager")
					.email("manager@mail.com").phone("0987654321").role(Role.manager).build();

			User user = User.builder().username("user").password("123456").fullName("Nguyen Van User")
					.email("user@mail.com").phone("0111222333").role(Role.user).build();

			userRepo.save(admin);
			userRepo.save(manager);
			userRepo.save(user);
		}

		// Insert Categories if not exists
		if (categoryRepo.count() == 0) {
			User admin = userRepo.findByUsername("admin");

			categoryRepo.save(Category.builder().name("Electronics").description("Electronic items").image(null)
					.createdBy(admin).build());

			categoryRepo.save(Category.builder().name("Books").description("Books and magazines").image(null)
					.createdBy(admin).build());

			categoryRepo.save(Category.builder().name("Clothes").description("Men and Women Clothes").image(null)
					.createdBy(admin).build());
		}

		// Insert Products if not exists
		if (productRepo.count() == 0) {
			List<Category> categories = categoryRepo.findAll();
			Category electronics = categories.stream().filter(c -> c.getName().equals("Electronics")).findFirst()
					.orElse(null);
			Category books = categories.stream().filter(c -> c.getName().equals("Books")).findFirst().orElse(null);

			if (electronics != null) {
				productRepo.save(Product.builder().productName("Laptop Dell").unitPrice(25000000.0).quantity(100)
						.category(electronics).build());
				productRepo.save(Product.builder().productName("Smartphone Samsung").unitPrice(15000000.0).quantity(200)
						.category(electronics).build());
			}
			if (books != null) {
				productRepo.save(Product.builder().productName("The Hobbit").unitPrice(350000.0).quantity(50)
						.category(books).build());
			}
		}
	}
}
