### S3 Client side encryption using [AWS Encryption SDK](https://github.com/aws/aws-encryption-sdk-java) and KMS
##### Java Spring-boot application exposing REST API endpoints to store and retreive objects to/from AWS S3. The file content is encrypted before being uploaded to S3 and decryption also takes place at the application level. This approach allows us to achieve both `at-rest` and `in-transit` encryption for our data while managing the encryption process at the application level and modifying it as per business requirements.

#### Demonstration Video

https://user-images.githubusercontent.com/69693621/167136277-854f2da0-175b-42c7-aa7d-7bd109bebe97.mov

### Local Setup
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

```
sdk install java 17-open
```
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

```
sdk install maven
```
* Create an S3 Bucket or use an existing | [Guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/creating-bucket.html)
* Create an IAM user with security credentials enabled and attach a permission containing the below given policy

```
{
    "Version": "2012-10-17",
    "Id": "S3ClientSideEncryptionPoc",
    "Statement": [
        {
            "Sid": "S3BasicCrudOperations",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:GetObject"
            ],
            "Resource": "arn:aws:s3:::<bucket-name-goes-here>/*"
        }
    ]
}
```
* Create a symmetric encryption KMS key in AWS KMS and provide permissions to the above created IAM user| [Guide](https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html)
* Clone the repository or this [individual subdirectory](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/INDIVIDUAL_FOLDER_CLONE.md)
* Configure the above created security credentials corresponding to the created user, the ARN of the KMS key and the bucket name in the `application.properties` file

```
# IAM Configuration
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>

# S3 Configuration
com.behl.aws.s3.bucket-name=<S3-Bucket-Name-Goes-Here>

# KMS Configuration
com.behl.aws.kms.key-arn=<KMS-Key-ARN-Goes-Here>
```
* Run the spring-boot application

```
mvn spring-boot:run
```

* API endpoints are accessible using below mentioned paths
```
POST http://localhost:8090/files
A file value in key with name file in form-data
```
```
GET http://localhost:8090/files/{objectKey}
```
