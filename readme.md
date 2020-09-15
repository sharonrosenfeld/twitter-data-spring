twitter-craft
-------------

- twitter-stream - retrieves twitts and persist to kafka
- twitter-sentiment - analyzes sentiemnt of received twitts and persist to kafka
- twitter-storage - retrieves the analyzed twitts from kafka and persist to ElasticSearch

twitter-stream -->Kafka->twitter-sentiment->Kafka->twitter-storage->ES 


How to spin all the services
------------------------ 
sudo docker-compose up -d
- note: upon startup, twitter-storage awaits 3 minutes for ES to be ready - pls be patient..

How To Query
------------
curl "http://localhost:8083/twitts/sentiment?product=Mint&from=2020-08-03T20:22:28&to=2020-10-05T20:22:28"