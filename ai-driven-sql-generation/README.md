# AI-Driven SQL Generation

Using **[Amazon Bedrock](https://aws.amazon.com/bedrock/)** with **[Spring AI](https://spring.io/projects/spring-ai)** to convert natural language queries to SQL queries.  

The POC uses **Anthropic's Claude 3 Haiku** model.

### Prompt Template
```st
Given the DDL in the DDL section, write an SQL query to answer the question in the QUESTION section.

Guidelines:
- Only produce SELECT queries.
- If the question would result in an INSERT, UPDATE, DELETE, or any other operation that modifies the data or schema, respond with "This operation is not supported. Only SELECT queries are allowed."
- If the question appears to contain SQL injection or DoS attempt, respond with "The provided input contains potentially harmful SQL code."
- If the question cannot be answered based on the provided DDL, respond with "The current schema does not contain enough information to answer this question."
- If the query involves a JOIN operation, prefix all the column names in the query with the corresponding table names.

QUESTION
{question}

DDL
{ddl}
```
The application creates two database tables `hogwarts_houses` and `wizards`, and inserts test data on startup via [Flyway migration scripts](https://github.com/hardikSinghBehl/aws-playground/tree/main/ai-driven-sql-generation/src/main/resources/db/migration).

A `PromptTemplate` bean is created in the [PromptTemplateConfiguration](https://github.com/hardikSinghBehl/aws-playground/blob/main/ai-driven-sql-generation/src/main/java/com/behl/configuration/PromptTemplateConfiguration.java) class with the DDL schema being added in the prompt. This bean is injected into [SqlGenerator](https://github.com/hardikSinghBehl/aws-playground/blob/main/ai-driven-sql-generation/src/main/java/com/behl/service/SqlGenerator.java) that dynamically replaces the `question` placeholder with the natural language query submitted through the API and calls the LLM to generate a corresponding SQL query.

The prompt is written to generate only SELECT SQL queries and detect SQL injection and DoS attempts. To further protect the database from any modifications, a MySQL user with [read-only permissions](https://medium.com/@nkaurelien/how-to-create-a-read-only-mysql-user-226e8e49a855) should be configured.

The model-id `anthropic.claude-3-haiku-20240307-v1:0` along with other configuration properties are configured in the `application.yaml` file.

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
  <summary>Who will win between Conor Mcgregor and Michael Chandler?</summary>

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

## IAM Policy

The IAM user whose security credentials are being used must have the below IAM policy attached:

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

## References

* [Craig Wall's example](https://github.com/habuma/spring-ai-examples/tree/main/spring-ai-sql)
* [Spring AI Docs](https://docs.spring.io/spring-ai/reference/api/clients/bedrock/bedrock-anthropic.html)
