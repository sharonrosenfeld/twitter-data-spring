
echo "waiting for ES.."

sleep 3m

curl -X PUT "elasticsearch:9200/analyzed-twitts"

curl -X PUT "elasticsearch:9200/analyzed-twitts/_settings" -H 'Content-Type: application/json' -d'{"index" : {"number_of_replicas" : 0}}'

curl -X PUT "elasticsearch:9200/analyzed-twitts/_mapping" -H 'Content-Type: application/json' -d'{"properties": {"sentiment": { "type":"text","fielddata": true},"createAt": {"type":"date"}}}'

java -jar twitts-storage.jar