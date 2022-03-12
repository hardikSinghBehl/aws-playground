### S3 encryption using customer managed AES encrypted secret key
#### References
* [Baeldung's article on AES Java encryption](https://www.baeldung.com/java-aes-encryption-decryption)
* [SSE-C](https://docs.aws.amazon.com/AmazonS3/latest/userguide/ServerSideEncryptionCustomerKeys.html)

#### Demonstration Screen-recording

https://user-images.githubusercontent.com/69693621/158022295-6c2b2055-e611-49d9-8026-a7b2f658f469.mov

----
### Local Setup
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

`sdk install java 17-open`
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

`sdk install maven`
* Create an IAM User with the below Policy
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
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
* Clone the repo
* Configure Security credentials and Bucket name in .properties file
```
com.behl.aws.s3.access-key=<IAM-users-access-key-goes-here>
com.behl.aws.s3.secret-key=<IAM-users-secret-access-key-goes-here>
com.behl.aws.s3.bucket-name=<s3-bucket-name-goes-here>
```
* Configure requirements to create secret key using below mentioned keys in .properties file 
```
# Secret-key configuration
com.behl.secret-key.value=idkPutASecureStringHere
com.behl.secret-key.salt=putALongRandomValueHere
```
* Run the spring-boot application

`mvn spring-boot:run`

* API endpoints are accessible using below mentioned paths
```
POST http://localhost:8080/files
A file value in key with name file in form-data
```
```
GET http://localhost:8080/files/{objectKey}
```
