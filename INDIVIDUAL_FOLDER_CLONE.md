### Guide on how to clone individual sub-folder(s) rather than the entire repository
* Initialize an empty git repository in a new blank directory

```
git init
```

* Add remote url (the below command fetches all the objects but doesn't check them out) [Change URL if forked]

```
git remote add -f origin https://github.com/hardikSinghBehl/aws-java-reference-pocs.git
```

* Execute the below command to enable [sparse checkout](https://git-scm.com/docs/git-sparse-checkout) 

```
git config core.sparseCheckout true
```
* Define the folder(s) to be cloned by executing the below command

```
echo "<folder-path-goes-here>" >> .git/info/sparse-checkout
```

Examples:

```
echo "aws-polly-integration" >> .git/info/sparse-checkout
```
```
echo "simple-notification-service-fanout/user-creation-service" >> .git/info/sparse-checkout
```

* Lastly, update the empty local repository with the state from the remote repository

```
git pull origin main
```
