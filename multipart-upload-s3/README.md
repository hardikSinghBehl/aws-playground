### Multipart-uploads and byte-range fetching in S3 to speed up uploads and downloads

It is a best practice to upload an object using multipart-upload if it exceedes the size of 100MB, and is mandatory for objects that are above 5GB. This is a simple Spring-boot POC that exposes an API to upload and retieve a file, the **minumum threshold** and **multipart-object-size** are both configured in the `application.properties` file, the object is uploaded in parts of configured size when the object size exceeds the threshold set.

The below mentioned properties can be modified as per requirements, both are used to create a bean of type TransferManager in [AwsS3Configuration.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/large-files-upload-directly-s3-put-presigned-uri/src/main/java/com/behl/offloader/configuration/AwsS3Configuration.java)

```
com.behl.aws.s3.multipart-threshold=100
com.behl.aws.s3.multipart-object-size=50
```

The Objects uploaded in multiparts have an ETag of more than 32 characters and end with `-#`, # signifying the number of parts the object was divided in.

#### References
* [Multipart-uploads](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html)
* [Byte-Range Fetches](https://docs.aws.amazon.com/whitepapers/latest/s3-optimizing-performance-best-practices/use-byte-range-fetches.html)

#### Demonstration Video

https://user-images.githubusercontent.com/69693621/167236052-29fdf8c9-11b6-471e-814d-0bf288309df4.mov

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
* Clone the repository or this [individual subdirectory](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/INDIVIDUAL_FOLDER_CLONE.md)
* Configure the above created security credentials corresponding to the created user and the bucket name in the `application.properties` file

```
# IAM Configuration
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>

# S3 Configuration
com.behl.aws.s3.bucket-name=<S3-Bucket-Name-Goes-Here>
com.behl.aws.s3.multipart-threshold=100
com.behl.aws.s3.multipart-object-size=50
```

* Run the spring-boot application

```
mvn spring-boot:run
```



