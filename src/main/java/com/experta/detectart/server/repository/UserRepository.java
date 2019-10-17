package com.experta.detectart.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.experta.detectart.server.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}