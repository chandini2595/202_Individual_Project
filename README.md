# Log Aggregator CLI

A command-line Java application that parses a `.txt` log file containing mixed log types (APM, Application, Request), classifies them, and produces structured JSON summaries for analysis.

---

## Objective

The goal is to build a CLI tool that processes a log file where each line may belong to a different category (APM, Application, or Request log). The application identifies the log type, extracts relevant fields, and performs aggregations like computing percentiles, counting severity levels, or calculating average performance metrics.

---

## Sample Log Input

```
timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72
timestamp=2024-02-24T16:22:20Z level=INFO message=“Scheduled maintenance starting” host=webserver1
timestamp=2024-02-24T16:22:25Z request_method=POST request_url=”/api/update” response_status=202 response_time_ms=200 host=webserver1
timestamp=2024-02-24T16:22:30Z metric=memory_usage_percent host=webserver1 value=85
timestamp=2024-02-24T16:22:35Z level=ERROR message=“Update process failed” error_code=5012 host=webserver1
```

---

##  Output Examples

### APM Logs (`apm.json`)

```
{
“cpu_usage_percent”: {
“minimum”: 60,
“median”: 78,
“average”: 77,
“max”: 90
},
“memory_usage_percent”: {
“minimum”: 5,
“median”: 10,
“average”: 13.5,
“max”: 78
}
}
```

###  Application Logs (`application.json`)

```
{
“ERROR”: 2,
“INFO”: 3,
“DEBUG”: 1,
“WARNING”: 1
}
```

### Request Logs (`request.json`)

```
{
“/api/status”: {
“response_times”: {
“min”: 100,
“50_percentile”: 150,
“90_percentile”: 180,
“95_percentile”: 200,
“99_percentile”: 250,
“max”: 300
},
“status_codes”: {
“2XX”: 2,
“4XX”: 1,
“5XX”: 1
}
}
}
```

---

## Usage

### CLI Command:
```bash
java -jar target/log-aggregator-1.0.0.jar --file input.txt
```

###  Input /  Output

- **Input**: Any `.txt` file with line-by-line log entries.
- **Output**:
  - `apm.json`
  - `application.json`
  - `request.json`
    
### Testing

- Unit tests written using **JUnit 5** for:
  - `ApmAggregatorTest` – validates metric aggregation (min/avg/median/max).
  - `ApplicationAggregatorTest` – counts log entries by severity.
  - `RequestAggregatorTest` – computes response time percentiles and HTTP status buckets.
 
Run all tests with 
```
mvn test
```

Manual validation was also performed by executing:
```
java -jar target/log-aggregator-1.0.0.jar --file input.txt
```

### Technologies used

	•	Java 17 – core language
	•	Maven – project/build management
	•	Jackson – JSON generation (jackson-databind)
	•	JUnit 5 – unit testing framework
	•	Maven Shade Plugin – for creating an executable fat JAR
