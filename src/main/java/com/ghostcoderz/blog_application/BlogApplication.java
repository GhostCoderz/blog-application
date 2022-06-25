package com.ghostcoderz.blog_application;

import com.ghostcoderz.blog_application.config.AppConstants;
import com.ghostcoderz.blog_application.entity.Role;
import com.ghostcoderz.blog_application.repository.RoleRepo;
import com.ghostcoderz.blog_application.utils.AppRunningThread;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

	private final RoleRepo roleRepo;

	public BlogApplication(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {

		Role adminRole = new Role();
		adminRole.setRoleId(AppConstants.ADMIN_USER_ROLE);
		adminRole.setName("ROLE_ADMIN");

		Role normalRole = new Role();
		normalRole.setRoleId(AppConstants.NORMAL_USER_ROLE);
		normalRole.setName("ROLE_NORMAL");

		List<Role> roles = new ArrayList<>();
		roles.add(adminRole);
		roles.add(normalRole);

		List<Role> result = this.roleRepo.saveAll(roles);

		AppRunningThread appRunningThread = new AppRunningThread();
		appRunningThread.start();

	}
}
