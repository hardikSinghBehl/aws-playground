### S3 Presigned URL
##### Java Spring-boot POC generating a 10 second preview of a private object stored in an S3 Bucket.

#### Demonstration Video

https://user-images.githubusercontent.com/69693621/154135569-20117b8d-30e5-4114-bc81-4235857e49be.mp4

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
            "Action": "s3:GetObject",
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

* Run the below command in core

`mvn clean install`

* To start the application, run any of the below command

`mvn spring-boot:run &`

* API to generate presigned URI for objects in the configured bucket can be accessed using the below URI
```
localhost:8080/premium/video/preview/{objectKey}
```




