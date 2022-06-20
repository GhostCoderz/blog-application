package com.ghostcoderz.blog_application.repository;

import com.ghostcoderz.blog_application.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
