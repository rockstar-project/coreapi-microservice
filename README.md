## Collection API

| API | Method | Endpoint |
|-----|--------|----------|
| Create Collection | POST | /collections |
| Get Collections | GET | /collections |
| Get Specific Collection | GET | /collections/{id} |
| Update Specific Collection | PATCH | /collections/{id} |
| Delete Specific Collection | DELETE | /collections/{id} |
| Create Collection Item | POST | /collections/{id}/items |
| Get Collection Items | GET | /collections/{id}/items |
| Get Specific Collection Item | GET | /collections/{id}/items/{item_id} |
| Update Specific Collection Item | PUT | /collections/{id}/items/{item_id} |
| Delete Specific Collection Item | DELETE | /collections/{id}/items/{item_id} |

### Getting Started


#### Install Docker Toolbox
* Download & Install [Docker Toolbox](https://www.docker.com/collections/docker-toolbox) on your computer.
* Click Docker Quickstart Terminal icon to open command line console

#### Configure Environment

```
$ export DOCKER_USER=xxxxx
$ export DOCKER_PASSWORD=xxxx
$ export DOCKER_NAMESPACE=rockstarproject
```

#### Download Code

```
$ git clone https://github.com/rockstar-project/coreapi-collection
$ cd coreapi-collection
```

#### Start virtual machine

```
$ docker-machine create -d virtualbox --virtualbox-memory 4096 coreapi-collection-vb-node
$ eval $(docker-machine env coreapi-collection-vb-node)
```

#### Run MySQL database

```
$ docker-compose up -d collectionmysql
```

#### Install Collection Schema

```
$ docker-compose -f docker-compose.develop.yml exec collectionmysql /bin/bash
$ mysql --user=rockstar --password=rockstar123 --database=rockstar_db_collection < collection/schema.sql
```

#### Run Discovery Service

```
$ docker-compose -f docker-compose.develop.yml up -d discovery
```

#### Run Collection API microservice

```
$ docker-compose -f docker-compose.develop.yml up -d collectionapi
```

#### Connect to the API endpoint

```
curl http://$(docker-machine ip coreapi-collection-vb-node):8080/collections | jq .
```

#### Redeploy with code changes

```
docker-compose stop collectionapi && docker-compose rm -f collectionapi
docker rmi $DOCKER_NAMESPACE/coreapi-collection
mvn clean package
docker build -t $DOCKER_NAMESPACE/coreapi-collection .
docker-compose up -d collectionapi
docker-compose logs --follow collectionapi
```
