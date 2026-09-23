# DevOps

* Master Build Status [![Master build](https://img.shields.io/github/actions/workflow/status/Rohan0784/DevOps/main.yml?branch=master&style=flat-square)](https://github.com/Rohan0784/DevOps/actions/workflows/main.yml?query=branch%3Amaster)
* Develop Build Status [![Develop build](https://img.shields.io/github/actions/workflow/status/Rohan0784/DevOps/main.yml?branch=develop&style=flat-square)](https://github.com/Rohan0784/DevOps/actions/workflows/main.yml?query=branch%3Adevelop)
* License [![License](https://img.shields.io/github/license/Rohan0784/DevOps)](https://github.com/Rohan0784/DevOps/blob/master/LICENSE)
* Release [![Releases](https://img.shields.io/github/release/Rohan0784/DevOps/all.svg?style=flat-square)](https://github.com/Rohan0784/DevOps/releases)

## Lab 2 build and run

The project uses Java 17, MongoDB Java driver 4.11.1, and MongoDB 7 in CI. The Maven manifest and Dockerfile use the project's existing package and artifact names consistently.

Run Maven's `package` lifecycle before building the application Docker image:

```shell
mvn --batch-mode clean package
docker build -t se_methods .
```

The application connects to `mongo-dbserver:27017`. For a container run, MongoDB and the application must share the `se-methods` network. On a machine where these have not been created yet:

```shell
docker network create --driver bridge se-methods
docker run -d --name mongo-dbserver --network se-methods -p 127.0.0.1:27000:27017 mongo:7
```

If the network and container already exist, reuse them and start the stopped MongoDB container. Once MongoDB is ready:

```shell
docker run --rm --network se-methods se_methods
```

A successful run prints the inserted document and exits successfully. The earlier local IntelliJ exercise uses `localhost:27000`; the committed application uses the lab's later container hostname, so a direct IntelliJ run needs that connection address adjusted for the local exercise.

## Development process

1. Pull the latest `develop` branch.
2. Create a feature branch from `develop`.
3. Complete the feature and package the application JAR.
4. Test the Docker configuration locally and through GitHub Actions.
5. Merge updated `develop` into the feature branch and test again.
6. Merge the completed feature into `develop` and push.
7. Create a release branch from `develop` when the release is ready.
8. Update the Maven version and Dockerfile JAR name together, then package and test.
9. Merge the release into `master`, push, tag the tested commit, and publish the GitHub release (mark alpha releases as prereleases).
10. Merge the release back into `develop` and push.

`master` is the release branch used by this lab; the repository also has an existing `main` branch. CI runs on all pushes, including feature and release branches, and on pull requests targeting `main`, `master`, or `develop`.
