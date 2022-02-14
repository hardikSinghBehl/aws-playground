### AWS S3 without storing/managing security credentials
##### POC to demonstrate using AWS S3 programatically using IAM Roles rather than security credentials (access key ID and secret access key). The spring-boot application will be deployed in an EC2 Instance (Required) and uses [S3](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-s3/1.12.158) and [STS](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-sts/1.12.158) dependencies.

----
### Setup
1. Create a S3 Bucket
2. Create an IAM Role with the below policy, allowing PutObject and ListBucket Actions on the above created bucket
```
{
    "Version": "2012-10-17",
    "Id": "S3UseWithIAMRolesAndSpringBoot",
    "Statement": [
        {
            "Sid": "S3AllowObjectCreationAndKeysRetreivalActions",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::<bucket-name-goes-here>",
                "arn:aws:s3:::<bucket-name-goes-here>/*"
            ]
        }
    ]
}
```
3. Attach the above created role on an EC2 Instance that will be used to run the application (Allow SSH and HTTP 8080 port in security groups of the instance)
4. Go to application.properties file and configure values of the below two mentioned properties
```
com.behl.aws.role-arn=<ARN-of-above-created-role>
com.behl.aws.s3.bucket-name=<name-of-s3-bucket-configured-above>
```
5. Start the application and test out the `POST /files` and `GET /files` API endpoints

----

### Important Classes
* [AwsProperties.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/s3-without-security-credentials/src/main/java/com/behl/grundy/properties/AwsProperties.java)
* [AwsCredentialProvider.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/s3-without-security-credentials/src/main/java/com/behl/grundy/bean/AwsCredentialProvider.java)
* [AwsSimpleStorageService.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/s3-without-security-credentials/src/main/java/com/behl/grundy/bean/AwsSimpleStorageService.java)
* [StorageController.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/s3-without-security-credentials/src/main/java/com/behl/grundy/controller/StorageController.java)

----
### APIs
1.) POST `/files`
  * Summary: Stores the given object into the configured S3 Bucket
  * Input (Required): A file form-data in the request-body with the key `file`

2.) GET `/files`
  * Summary: Returns list of all object keys in the configured S3 Bucket

----

### Deployment/System Requirements
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

`sdk install java 17-open`
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

`sdk install maven`

* Clone the repo and run the below command in project's core

`mvn clean install`

* To start the application, run the below commands (control + (A+D) to detach screen)

`mvn spring-boot:run &`
