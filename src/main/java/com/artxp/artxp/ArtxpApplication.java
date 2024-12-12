package com.artxp.artxp;

import com.artxp.artxp.domain.entities.Role;
import com.artxp.artxp.domain.entities.UsuarioEntity;
import com.artxp.artxp.domain.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ArtxpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtxpApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
		return args -> {
			if(usuarioRepository.findByEmail("admin@admin.com").isEmpty()){
				UsuarioEntity admin = new UsuarioEntity();
				admin.setEmail("admin@admin.com");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setName("admin");
				admin.setLastname("admin");
				admin.setRol(Role.ADMIN);
				usuarioRepository.save(admin);
			}
		};
	}
}
