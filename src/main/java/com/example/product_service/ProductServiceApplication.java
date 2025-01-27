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
			ProductEntity product3 = new ProductEntity("Bose Soundlink Color", "Speaker", 331.90, 4);
			productRepository.save(product3);
			ProductEntity product4 = new ProductEntity("Samsung Galaxy S24", "Cellphone", 999.90, 6);
			productRepository.save(product4);
			ProductEntity product5 = new ProductEntity("Iphone 16 Pro", "Cellphone", 1999.99, 9);
			productRepository.save(product5);
			ProductEntity product6 = new ProductEntity("JBL BoomBox", "Speaker", 800.00, 20);
			productRepository.save(product6);


			System.out.println("Product Server Running!");
		};
	}
}
