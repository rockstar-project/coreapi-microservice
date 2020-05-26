## Microservices API


mongo $MONGODB_DATABASE -u $MONGODB_USER -p$MONGODB_PASSWORD

oc exec -it mongodb-1-jzsgf -- bash -c "mongo -u sagy -p password testdb --eval \"db.redhat.find().pretty()\""