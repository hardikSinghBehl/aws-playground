### Cipherinator: Envelope Encryption using KMS

Envelope Encryption combines the benefits of Symmetric and Asymmetric Key Encryption Algorithms by providing the performance of symmetrical encryption with the security of asymmetrical encryption algorithms.

A single symmetric key is used to generate a [data-key pair](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#data-key-pairs) which contains a plaintext key and it's corresponding cipher-key (encrypted key). The plaintext key can be used to encrypt the data on the server and be disregarded after the encryption operation, the encrypted data can be stored alongside the encrypted data key safely without any worry because the data key is inherently protected by encryption.

`In general, symmetric key algorithms are faster and produce smaller ciphertexts than public key algorithms. But public key algorithms provide inherent separation of roles and easier key management. Envelope encryption lets you combine the strengths of each strategy`

In this POC, Envelope Encryption is used to store user's password in the database along with the corresponding cipher key `(encryption at-rest)`
, the stored password is decrypted and validated against the entered password during /login. The plain-text data key is obtained by performing `kms:decrypt` action using the cipher-key. This encryption process can be used in other contexts as well as per business requirements.
#### Important classes
* [EncryptionService.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/envelope-encryption/src/main/java/com/behl/cipherinator/service/EncryptionService.java)
* [AwsKmsConfigurationProperties.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/envelope-encryption/src/main/java/com/behl/cipherinator/properties/AwsKmsConfigurationProperties.java)
* [UserService.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/envelope-encryption/src/main/java/com/behl/cipherinator/service/UserService.java)

#### References
* [Envelope Encryption using CMK](https://docs.aws.amazon.com/wellarchitected/latest/financial-services-industry-lens/use-envelope-encryption-with-customer-master-keys.html)
* [Envelope Encryption Workshop](https://catalog.us-east-1.prod.workshops.aws/workshops/aad9ff1e-b607-45bc-893f-121ea5224f24/en-US/keymanagement-kms/envelope-encryption)
* [How to Manage Encryption at Scale with Envelope Encryption & Key Management Systems](https://www.freecodecamp.org/news/envelope-encryption/)
* [[VIDEO] Use envelope encryption with data keys to protect messaging and streaming data](https://www.youtube.com/watch?v=ilA4Jftit2Y)

#### Demonstration Video

https://user-images.githubusercontent.com/69693621/167779423-e48a5403-1cfb-49b5-a544-531d15295cfe.mov

---

### Local Setup
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

```
sdk install java 17-open
```
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

```
sdk install maven
```
* Create an IAM user with security credentials (programmatic-access) enabled | [Guide](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html)
* Create a KMS Symmetric Key or use an existing one with the above created IAM user registered as a key-user to inherit access to basic encryption actions | [Guide](https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html)
* Clone the repository or this [individual subdirectory](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/INDIVIDUAL_FOLDER_CLONE.md)
* Configure the above created security credentials corresponding to the IAM user and [keyID](https://docs.aws.amazon.com/kms/latest/developerguide/find-cmk-id-arn.html) of the KMS key in the `application.properties` file

```
# IAM Configuration
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>

# KMS Configuration
com.behl.aws.kms.key-id=<KMS-Key-ARN-Goes-Here>
```
* Run the spring-boot application

```
mvn spring-boot:run
```
