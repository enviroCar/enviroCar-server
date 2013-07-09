---
layout: default
---
	
# /rest/measurements #

This is the measurements resource.

### GET

Get a list of all measurements. 

Query parameters:

| Name    | Type  | Default | Details
|---------|-------|---------|--------
| `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

#### Response Schema
{% include_schema measurememts %}

#### Response Example
{% include_example measurements %}

### POST

Creates new measurements.

#### Response Example
{% include_example measurement.create %}	
	
# /rest/measurements/:measurementid #

### GET

Get the measurement `measurementid`.

### Response Schema
{% include_schema measurement %}

### Response Example
{% include_example measurement %}

### DELETE /rest/measurements/:measurementid`

Deletes the measurement `measurementid`.
