# DigitalPhoneBookUi

## Project information

This backend application uses Spring framework and PostgresDB running on docker container.

This application accepts prompts in String, written through natural language format, which are then passed to the application's REST API. 
Then with help of OpenAI, prompt are interpreted, and are passed to the appropriate methods.

There is also endpoint to fetch all available records.

Complete API documentation is listed further in README file.

## Tech Stack 🛠️

* Java
* Spring Boot
* REST API
* OpenAI Integration
* Postgres
* Docker


## Running server
1. Run environment backend by docker compose to start the DB server:
      `docker compose up`

2. Run application http://localhost:8080, remember to do the following:
    - use `-Dspring.profiles.active=dev` flag for development environment
    - create `.env` file in root location of the project, and add there key/value property with `OPENAI_API_KEY` as key, and OpenAI API key as value.
   
## Running tests
1. When running tests, remember to do the following:
    - use `-Dspring.profiles.active=test` flag for testing environment


# Command Interpretation API

This service provides REST endpoints to interpret natural language commands using OpenAI and to manage stored records.

---

## REST API

### Base URL

```
http://localhost:8080/api/v.1.0
```

All endpoints consume and produce **JSON**.

---

## 🔹 Interpret Command

Interprets a natural language prompt into a structured command using OpenAI and executes it.

### Endpoint

```
POST /record
```

### Request Body

```json
{
  "prompt": "string"
}
```

| Field  | Type   | Required | Description                           |
| ------ | ------ | -------- | ------------------------------------- |
| prompt | string | Yes      | Natural language command to interpret |

### Example Request

```json
{
  "prompt": "Create a new record for Joanna, number is 123456789"
}
```

### Response

```json
[
  {
    "name": "Joanna",
    "number": "123456789"
  }
]
```

| Field     | Type          | Description                                           |
| --------- | ------------- | ----------------------------------------------------- |
| record    | object        | Record data affected by the operation                 |
| operation | string (enum) | Executed operation type (e.g. CREATE, UPDATE, DELETE) |
| message   | string        | Human-readable execution message                      |

### HTTP Status Codes

* `200 OK` – Command interpreted and executed successfully
* `400 Bad Request` – Invalid request payload
* `500 Internal Server Error` – Command interpretation or execution failed

---

## 🔹 Get All Records

Retrieves all stored records.

### Endpoint

```
GET /record/all
```

### Request Parameters

None

### Response

```json
[
  {
    "name": "John",
    "number": "000000000"
  },
  {
    "name": "Joanne",
    "number": "123456789"
  }
]
```

### Response Fields

| Field   | Type   | Description   |
|---------| ------ |---------------|
| name    | string | Record name   |
| number  | string | Record number |

### HTTP Status Codes

* `200 OK` – Records retrieved successfully
* `500 Internal Server Error` – Failed to retrieve records

---

## ✅ Notes

* All endpoints are synchronous
* Error handling follows standard HTTP status conventions through controller advisor



