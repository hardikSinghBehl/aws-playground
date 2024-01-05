package com.behl.cipherinator.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "users")
public class User {

	@DynamoDBHashKey
	@DynamoDBAttribute(attributeName = "user_name")
	private String userName;

	@DynamoDBAttribute(attributeName = "password")
	private String password;

	@DynamoDBAttribute(attributeName = "encrypted_data_key")
	private String encryptedDataKey;

}
