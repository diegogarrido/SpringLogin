package com.codingdojo.java.model.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codingdojo.java.model.User;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	
}
