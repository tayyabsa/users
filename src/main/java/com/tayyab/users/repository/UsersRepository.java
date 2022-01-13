package com.tayyab.users.repository;

import com.tayyab.users.entity.UsersEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity,Long> {

    Optional<UsersEntity> findByUserName(String userName);

    List<UsersEntity> findAll(Specification<UsersEntity> specification);
}
