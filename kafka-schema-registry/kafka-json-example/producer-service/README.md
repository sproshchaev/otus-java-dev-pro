# kafka-json-example 
### producer-service

```bash
curl -X POST http://localhost:8080/api/persons \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice", "age": 30, "email": "alice@example.com"}'
```