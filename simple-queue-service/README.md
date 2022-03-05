### [AWS Simple Queue Service](https://aws.amazon.com/sqs/) Integration with Java Spring-boot
One application is acting as a publisher which [publishes](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/simple-queue-service/sqs-publisher/src/main/java/com/behl/sqs/publisher/SqsPublisherApplication.java) a message to the queue after every 4 seconds, the other Service [consumes](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/simple-queue-service/sqs-consumer/src/main/java/com/behl/sqs/consumer/SqsConsumerApplication.java) the messages from the queue asynchronously and logs it.

---

#### Setup Required
* Install Java 11+ and Maven ([SdkMan](https://sdkman.io) is recommended)
* Create an IAM user with programmatic access and attach the below mentioned policy
```
{
    "Version": "2012-10-17",
    "Id": "sqs-consumption-permissions"
    "Statement": [
        {
            "Sid": "sqs-consumption-permissions-1",
            "Effect": "Allow",
            "Action": [
                "sqs:GetQueueUrl",
                "sqs:ReceiveMessage",
                "sqs:GetQueueAttributes",
                "sqs:ListQueueTags"
            ],
            "Resource": "arn:aws:sqs:<Region>:<Account-Id>:<Queue-Name>"
        },
        {
            "Sid": "sqs-consumption-permissions-2",
            "Effect": "Allow",
            "Action": "sqs:ListQueues",
            "Resource": "*"
        }
    ]
}
```
* Create a Queue in SQS and provide the ARN of the above IAM user in the access-policy tab (secret-superhero-information is used in this POC)
* Configure the appropiate values configured in both the consumer and publisher's .properties file

----

#### Demonstration Recording

https://user-images.githubusercontent.com/69693621/156875488-0b0396b6-b754-499d-bf09-fa736f37fc50.mov
