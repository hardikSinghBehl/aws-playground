#!/bin/bash
table_name="users"
hash_key="user_name"

awslocal dynamodb create-table \
    --table-name "$table_name" \
    --key-schema AttributeName="$hash_key",KeyType=HASH \
    --attribute-definitions AttributeName="$hash_key",AttributeType=S \
    --billing-mode PAY_PER_REQUEST

echo "DynamoDB table '$table_name' created successfully with hash key '$hash_key'"
echo "Executed init-dynamodb.sh"
