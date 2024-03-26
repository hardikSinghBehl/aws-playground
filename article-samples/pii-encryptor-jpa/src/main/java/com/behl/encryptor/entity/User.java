package com.behl.encryptor.entity;

import java.util.UUID;

import com.behl.encryptor.annotation.Encryptable;
import com.behl.encryptor.annotation.EncryptedDataKey;
import com.behl.encryptor.entity.listener.FieldEncryptionListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(FieldEncryptionListener.class)
public class User {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Encryptable
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Encryptable
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@Encryptable
	@Column(name = "address", nullable = false)
	private String address;

	@Encryptable
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;

	@EncryptedDataKey
	@Column(name = "encrypted_data_key", nullable = false)
	private String encryptedDataKey;

}
