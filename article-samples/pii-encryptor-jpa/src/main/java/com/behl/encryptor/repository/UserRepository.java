package com.behl.encryptor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.behl.encryptor.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
