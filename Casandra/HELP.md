# Getting Started

### Reference Documentation

http://localhost:8080/swagger-ui.html

### Each user_id defines a partition. Within each partition, rows are ordered by timestamp DESC, then by activity_id.

“I choose NetworkTopologyStrategy with a replication factor of 3 
to ensure high availability and fault tolerance. For consistency, 
I balance tradeoffs—using LOCAL_QUORUM for user-facing operations where consistency matters, 
and ONE for non-critical queries where performance is key.”



