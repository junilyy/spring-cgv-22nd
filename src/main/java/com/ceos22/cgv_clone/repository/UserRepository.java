package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
