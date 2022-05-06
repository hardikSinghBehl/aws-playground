### S3 Presigned URL to upload an Object
This architecture can be used to offload the backend server from the task of large object uploads, hence reducing the CPU Utilization and RAM usage. A Presigned URI can be used to provide access to the client, to upload directly into the configured S3 Bucket. Temporary credentials are embedded into the URI itself and the client inherits all the permissions attached to the creater IAM user.

#### Demonstration Video

https://user-images.githubusercontent.com/69693621/167198124-df1fcb52-dac1-424e-8b48-8f828f32e0b2.mov


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
    "Id": "S3PresignedPutUriPoc",
    "Statement": [
        {
            "Sid": "S3ObjectUploadOperation",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject"            ],
            "Resource": "arn:aws:s3:::<bucket-name-goes-here>/*"
        }
    ]
}
```
* Clone the repository or this [individual subdirectory](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/INDIVIDUAL_FOLDER_CLONE.md)
* Configure the above created security credentials corresponding to the created user and the bucket name in the `application.properties` file

```
# IAM Configuration
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>

# S3 Configuration
com.behl.aws.s3.bucket-name=<S3-Bucket-Name-Goes-Here>
com.behl.aws.s3.presigned-uri-expiration=<Validity-Time-Period-Of-Presigned-URI>
```
* Run the spring-boot application

```
mvn spring-boot:run
```

* API endpoint is accessible on the below mentioned path, and can be used to generate a presigned URI against the provided objectKey in path variable

```
GET http://localhost:8080/v1/temporary/upload/{objectKey}
```
```
EXAMPLE: curl localhost:8080/v1/temporary/upload/index.html
```
