package com.behl.cipherinator.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.behl.cipherinator.entity.User;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public void save(@NonNull final User user) {
		dynamoDBMapper.save(user);
	}

	public Optional<User> findByUserName(@NonNull final String userName) {
		final var user = dynamoDBMapper.load(User.class, userName);
		return Optional.ofNullable(user);
	}

	public boolean existsByUserName(@NonNull final String userName) {
		final var user = findByUserName(userName);
		return user.isPresent();
	}

}
