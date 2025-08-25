rootProject.name = "otus-java-dev-pro"
include("jdbc")
include("jpql")

include("kafka-schema-registry")
include("kafka-schema-registry:kafka-json-example")
include("kafka-schema-registry:kafka-avro-example")
include("kafka-schema-registry:kafka-avro-example:producer-service")
include("kafka-schema-registry:kafka-json-example:producer-service")
include("kafka-schema-registry:kafka-avro-example:consumer-service")
include("kafka-schema-registry:kafka-json-example:consumer-service")