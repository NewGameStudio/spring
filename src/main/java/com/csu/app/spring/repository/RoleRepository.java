package com.csu.app.spring.repository;

import com.csu.app.spring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
