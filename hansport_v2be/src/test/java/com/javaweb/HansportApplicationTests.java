package com.javaweb;

import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:hansport;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.show-sql=false",
		"app.seed.enabled=true",
		"hansport.upload-file.base-path=target/test-upload"
})
class HansportApplicationTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {
		Assertions.assertTrue(roleRepository.existsByName("ADMIN"));
		Assertions.assertTrue(roleRepository.existsByName("USER"));
		Assertions.assertTrue(userRepository.existsByEmail("admin@hansport.local"));
		Assertions.assertTrue(productRepository.existsByName("Giày đá bóng 11Play Pro"));
	}

}
