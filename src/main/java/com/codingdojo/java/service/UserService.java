package com.codingdojo.java.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingdojo.java.model.User;
import com.codingdojo.java.model.dao.UserDAO;

@Service
public class UserService {
	@Autowired
	private final UserDAO userDao;

	public UserService(UserDAO userDao) {
		this.userDao = userDao;
	}

	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return userDao.save(user);
	}

	public User findByEmail(String email) {
		Optional<User> user = userDao.findByEmail(email);
		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}

	public User findUserById(Integer id) {
		Optional<User> u = userDao.findById(id);
		if (u.isPresent()) {
			return u.get();
		} else {
			return null;
		}
	}

	public boolean authenticateUser(String email, String password) {
		Optional<User> user = userDao.findByEmail(email);
		if (!user.isPresent()) {
			return false;
		} else {
			if (BCrypt.checkpw(password, user.get().getPassword())) {
				return true;
			} else {
				return false;
			}
		}
	}
}
