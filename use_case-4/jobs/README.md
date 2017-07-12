# Initial setup

1. Add the account associated with this jenkins server via `GITHUB_TOKEN` with access to your repo.
2. Add your Jenkins pipeline groovy script to the jobs repo folder
3. Configure the webhook for your GitHub repository for builds to trigger
4. :pencil2: Create a Jenkinsfile at the root of your GitHub repository

***

### 1. Add GITHUB_TOKEN service account

In your GitHub repository, add user the account associated with `GITHUB_TOKEN` as a contributor.
This is a service account, which needs to have write access to the repository
so that it can provide
[commit statuses](https://github.com/blog/1227-commit-status-api) back.

***

### 2. Register your job into ``jobs/jobs_dsl/*``

  - Go to ``job_dsl`` directory and create a new directory with
    the name of your GitHub Organization.
  - Then create a new file like ```my_org_name.groovy``` and copy the content from the template below.

```
organizationFolder('MY-ORGANIZATION-NAME') {
    authorization {
            permissionAll('yourusername')
            permission('hudson.model.Item.Read', 'anonymous')
            permission('hudson.model.Item.Build', 'yourusername')
         }
    organizations {
        github {
            apiUri('https://api.github.com')
            checkoutCredentialsId('github-creds')
            includes('*')
            repoOwner('MY-ORGANIZATION-NAME')
            scanCredentialsId('github-creds')
            pattern('.*')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(20)
        }
    }
}
```

This groovy file describes, with code, the creation of your job in Jenkins. You will add one file per
GitHub Organization.

  - Update ```organizationFolder``` and ```repoOwner``` with the name of your GitHub ORG
  - If you only want to build an specific repo modify ```pattern``` like ```pattern('repo_name1|repo_name2|repo_name3')```
  - Update permissions for your job on ```authorization```, make sure you set github usernames (not hpe email).
  - Submit the **Pull Request**

For the complete reference on this file go to -> [JobDSL reference docs](https://jenkinsci.github.io/job-dsl-plugin/#path/organizationFolder)


***

### 3. Configure Webhook for Push & Pull Requests event into branches
1. Go to the Settings tab in your repository
1. Hit on Hooks & Services Option
1. Hit on Add Webhook
1. In the payload URL set this value:
  * ```https://yourjenkins.host.com/github-webhook/```
1. In the Content type set:
  * ```application/x-www-form-urlcoded```
1. NOTE: Make sure you click on **"Disable SSL Verification"**
1. From the trigger events options hit on:
  * ```Send me everything```
1. Then save it.

***

### 4. :pencil2: Create Jenkinsfile

The Jenkinsfile contains the steps of the Jenkins pipeline. you can use these [default template](template.md) or build your own.


Done, have a :coffee: and be happy to save power and CO2 :evergreen_tree::deciduous_tree:
by leveraging shared resources!
