#!/bin/bash
set -e

echo "🚀 Starting Cassandra 3-node cluster..."
docker-compose up -d

echo "⏳ Waiting for cassandra1 to be ready..."

RETRY_COUNT=0
MAX_RETRIES=40

until docker exec cassandra1 cqlsh -e "DESCRIBE keyspaces;" > /dev/null 2>&1; do
  RETRY_COUNT=$((RETRY_COUNT+1))
  if [ "$RETRY_COUNT" -ge "$MAX_RETRIES" ]; then
    echo "❌ cassandra1 did not become ready in time. Exiting."
    exit 1
  fi
  echo "⌛ Waiting for cassandra1... ($RETRY_COUNT/$MAX_RETRIES)"
  sleep 5
done

echo "✅ cassandra1 is ready."

echo "⏳ Waiting for cluster to form with 3 UP nodes..."

RETRY_COUNT=0

until [ "$(docker exec cassandra1 nodetool status | grep '^UN' | wc -l)" -eq 3 ]; do
  RETRY_COUNT=$((RETRY_COUNT+1))
  if [ "$RETRY_COUNT" -ge "$MAX_RETRIES" ]; then
    echo "❌ Cluster did not form with 3 UP nodes in time. Exiting."
    exit 1
  fi
  echo "⌛ Waiting for cluster formation... ($RETRY_COUNT/$MAX_RETRIES)"
  sleep 5
done

echo "✅ Cluster is fully formed with 3 nodes UP."

# Optional: check cluster status
echo "📡 Checking cluster status from cassandra1:"
docker exec cassandra1 nodetool status || echo "⚠️ nodetool not ready yet."

# Run init.cql if present
if [ -f init.cql ]; then
  echo "📜 Running init.cql..."
  docker exec -i cassandra1 cqlsh < init.cql
  echo "✅ init.cql executed successfully."
else
  echo "⚠️ init.cql not found. Skipping schema creation."
fi
