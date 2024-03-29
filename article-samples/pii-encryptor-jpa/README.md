A proof of concept demonstrating encryption and decryption of sensitive PII/PHI fields prior to their storage and retrieval from the database respectively.

### Problem statement to solve
Entity classes should have the ability to annotate PII/PHI fields using a custom annotation. The operation of encrypting these fields before storing them and decrypting them after loading them from the database should happen automatically, without direct intervention from either the service or repository layers.

### Proposed solution
The proposed solution employs the following components to solve the problem statement: 

* [AWS KMS](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#enveloping) : Envelope encryption via AWS KMS is leveraged to encrypt/decrypt fields marked as sensitive.

* [JPA Entity Listeners](https://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/listeners.html) : Using `@EntityListeners`, we can tap into the entity lifecycle events and alter the object in context. Lifecycle events `@PrePersist` and `@PostLoad` can be used to perform encryption and decryption respectively.

Sensetive fields within the entity classed can be declared using the `@Encryptable` annotation. And the entity class should be annotated with `@EntityListeners(FieldEncryptionListener.class)`

Since we're using envelope encryption, the encrypted data key is needed to be stored with the database record as well. The annotation `@EncryptedDataKey` must be applied to a field within the entity responsible for storing this value.

The solution can be further understood by examining the Test class [DataEncryptionIT.java](https://github.com/hardikSinghBehl/aws-playground/blob/main/article-samples/pii-encryptor-jpa/src/test/java/com/behl/encryptor/DataEncryptionIT.java) which uses Testcontainers and LocalStack to spin up MySQL and AWS KMS for testing.

### Example Entity class
The following example illustrates field encryption using the provided annotations:
```java
@Getter
@Setter
@Entity
@Table(name = "appointments")
@EntityListeners(FieldEncryptionListener.class)
public class Appointment {
	
	@Id
	private UUID id;
	
	@Encryptable
	private String patientName;

	@Encryptable
	private String diagnosis;

	@Encryptable
	private String treatmentPlan;
	
	@EncryptedDataKey
	@Setter(AccessLevel.NONE)
	private String dataKey;
	
}
```

### Note
* The decryption post loading the object from the database is only successful, if the full entity is loaded either through CrudRepositoy, JpaRepostory, EntityManager or Query operations. Decryption on partial loading would be possible with service layer intervention, but this case is not considered in the POC at this time.
* PII fields such as email addresses should be hashed, not encrypted in scenarios where the value is required for processes such as login, password recovery, etc.
* PII/PHI fields that need to be queried should also be hashed and not encrypted, preferably stored in separate columns.
