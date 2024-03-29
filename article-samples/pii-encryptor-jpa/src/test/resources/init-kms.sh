#!/bin/bash
key_id="00000000-1111-2222-3333-000000000000"

awslocal kms create-key --tags '[ { "TagKey" : "_custom_id_" , "TagValue" : "'$key_id'" } ]'

echo "KMS key with keyID '$key_id' created successfully"
echo "Executed init-kms.sh"