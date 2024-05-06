# AI-Driven SQL Generation

Proof-of-concept demonstrating the usage of **Spring AI and Amazon Bedrock** to convert plain english text from user into SQL queries that can be executed against the application's database schema.

The POC uses **Anthropic's Claude 3 Haiku** model whose model-id `anthropic.claude-3-haiku-20240307-v1:0` is configured in the `application.yaml` file.

## Key files
* [Prompt.st](https://github.com/hardikSinghBehl/aws-playground/blob/main/ai-driven-sql-generation/src/main/resources/prompt.st)
* [Flyway Migration Scripts](https://github.com/hardikSinghBehl/aws-playground/tree/main/ai-driven-sql-generation/src/main/resources/db/migration)
* [PromptTemplateConfiguration.java](https://github.com/hardikSinghBehl/aws-playground/blob/main/ai-driven-sql-generation/src/main/java/com/behl/configuration/PromptTemplateConfiguration.java)
* [SqlGenerator.java](https://github.com/hardikSinghBehl/aws-playground/blob/main/ai-driven-sql-generation/src/main/java/com/behl/service/SqlGenerator.java)

## Examples

The sole API can be invoked using [httpie](https://httpie.io/) with the below command:

```shell
http POST :8080/api/v1/query question=""
```

<details>
  <summary>Give me 3 wizard names and their blood status that belonged to a house founded by Salazar Slytherin</summary>

---
#### Query
```sql
SELECT wizards.name, wizards.blood_status
FROM wizards
JOIN hogwarts_houses ON wizards.house_id = hogwarts_houses.id
WHERE hogwarts_houses.founder = 'Salazar Slytherin'
LIMIT 3;
```

#### API Response

```json
{
  "result": [
    [
      "Draco Malfoy",
      "Pure Blood"
    ],
    [
      "Tom Riddle",
      "Half blood"
    ],
    [
      "Bellatrix Lestrange",
      "Pure Blood"
    ]
  ]
}
```
</details>

<details>
  <summary>Give me details of a wizard whose name begins with 'does not' and ends with 'exist'.</summary>

---

#### SQL Query

```sql
SELECT *
FROM wizards
WHERE name LIKE 'does not%exist';
```

#### API Response

```json
{
  "detail": "No results found for the provided query.",
  "instance": "/api/v1/query",
  "status": 404,
  "title": "Not Found",
  "type": "about:blank"
}
```
</details>

<details>
  <summary>What are some healthy and tasty alternatives to traditional pasta dishes?</summary>

---

#### API Response

```json
{
  "detail": "The current schema does not contain enough information to answer this question.",
  "instance": "/api/v1/query",
  "status": 400,
  "title": "Bad Request",
  "type": "about:blank"
}
```
</details>

<details>
  <summary>Create a new wizard record named 'Hardik Behl' belonging to Slytherin house.</summary>

---

#### API Response

```json
{
  "detail": "This operation is not supported. Only SELECT queries are allowed.",
  "instance": "/api/v1/query",
  "status": 400,
  "title": "Bad Request",
  "type": "about:blank"
}
```
</details>

<details>
  <summary>What was the name and animal of the house whose colors were Scarlet and Gold?</summary>

---
#### Query
```sql
SELECT name, animal_symbol
FROM hogwarts_houses
WHERE house_colors = 'Scarlet and Gold';
```

#### API Response

```json
{
  "result": [
    [
      "Gryffindor",
      "Lion"
    ]
  ]
}
```
</details>

<details>
  <summary>What is the blood status of the wizard named '; DROP TABLE wizards;--'</summary>

---

#### API Response

```json
{
  "detail": "The provided input contains potentially harmful SQL code.",
  "instance": "/api/v1/query",
  "status": 400,
  "title": "Bad Request",
  "type": "about:blank"
}
```
</details>

## Local Setup

The application can be set up locally using Docker with the below commands:

```shell
sudo docker-compose build
```

```shell
sudo AWS_ACCESS_KEY=value AWS_REGION=value AWS_SECRET_KEY=value docker-compose up -d
```

### IAM Policy

The IAM user whose security credentials are being used, must have the below IAM policy attached:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "bedrock:InvokeModel",
      "Resource": "arn:aws:bedrock:your-region::foundation-model/anthropic.claude-3-haiku-20240307-v1:0"
    }
  ]
}
```

## Reference

This was inspired from Craig Wall's [repository](https://github.com/habuma/spring-ai-examples/tree/main/spring-ai-sql).
