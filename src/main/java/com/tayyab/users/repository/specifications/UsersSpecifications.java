package com.tayyab.users.repository.specifications;

import com.tayyab.users.entity.UsersEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UsersSpecifications {

  public static Specification<UsersEntity> isLongTermUser() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("createdAt"), LocalDateTime.now().minusYears(2));
  }
}
