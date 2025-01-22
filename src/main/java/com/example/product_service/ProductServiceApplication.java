package com.example.product_service;

import com.example.product_service.models.ProductEntity;
import com.example.product_service.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ProductRepository productRepository) {
		return args -> {

			ProductEntity product = new ProductEntity("Motorolla G9", "Cellphone", 560.50, 8);
			productRepository.save(product);
			ProductEntity product1 = new ProductEntity("Ipad Pro", "Tablet", 1500.87, 5);
			productRepository.save(product1);
			ProductEntity product2 = new ProductEntity("HP Victus 2024", "NoteBook", 1399.99, 15);
			productRepository.save(product2);
			ProductEntity product3 = new ProductEntity("Bose Soundlink Color", "Speaker", 331.90, 1);
			productRepository.save(product3);


			System.out.println("Product Server Running!");
		};
	}
}
