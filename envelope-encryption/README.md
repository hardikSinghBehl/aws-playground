### Envelope Encryption using AWS KMS

Envelope Encryption efficiently combines the benefits of both Symmetric and Asymmetric Key Encryption Algorithms, providing rapid performance of the former and enhanced security of the latter.

A single symmetric key known as the root key is used to generate a [data-key pair](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#data-key-pairs) which contains a plaintext key (DEK) and it's corresponding cipher-key (encrypted version of DEK). The plaintext key is used to encrypt the data on the server post which it is discarded i.e erased from the memory, now the encrypted data can be stored alongside the encrypted data key safely in the database because the data key is inherently protected by encryption.

In this practical proof of concept, Envelope encryption is used to encrypt the user's password before storing it alongside the encrypted data key in a DynamoDB table. The stored password is decrypted and validated against the password provided by the user during login. The plain-text data key is obtained by performing `kms:decrypt` action on the stored cipher-key. This encryption process can be used in other contexts as well as per business requirements.

#### Important classes
* [EnvelopeEncryptionService.java](https://github.com/hardikSinghBehl/aws-playground/blob/main/envelope-encryption/src/main/java/com/behl/cipherinator/service/EnvelopeEncryptionService.java)
* [UserService.java](https://github.com/hardikSinghBehl/aws-playground/blob/main/envelope-encryption/src/main/java/com/behl/cipherinator/service/UserService.java)

#### Testing
LocalStack module of [Testcontainers](https://java.testcontainers.org/modules/localstack/) is leveraged for testing the application. Integration tests can be executed with the command `mvn integration-test`. 

[Initialization Hooks](https://docs.localstack.cloud/references/init-hooks/) are used to execute the [shell scripts](https://github.com/hardikSinghBehl/aws-playground/tree/main/envelope-encryption/src/test/resources) that provision the required KMS key and DynamomDB table in the Localstack instance.

#### Local Setup
The application can be started locally using Docker with the below commands. [Localstack](https://www.localstack.cloud/) is used to create required AWS resources eliminating the need of provisioning actual AWS services.

```bash
sudo docker-compose build
```
```bash
sudo docker-compose up -d
```

#### References
* [Envelope Encryption AWS Workshop](https://catalog.us-east-1.prod.workshops.aws/workshops/aad9ff1e-b607-45bc-893f-121ea5224f24/en-US/keymanagement-kms/envelope-encryption)
* [How to Manage Encryption at Scale with Envelope Encryption & Key Management Systems](https://www.freecodecamp.org/news/envelope-encryption/)
* [[VIDEO] Use envelope encryption with data keys to protect messaging and streaming data](https://www.youtube.com/watch?v=ilA4Jftit2Y)

#### Visual Walkthrough

https://github.com/hardikSinghBehl/aws-playground/assets/69693621/a8ee23e6-83e2-4a47-aab8-85b40ebf1b68
