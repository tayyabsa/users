package com.tayyab.users.repository;

import com.tayyab.users.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByRole(String role);
}
