---
layout: default
---

# /rest/measurements
### GET
Get a list of all measurements.
#### Response Schema
{% include_schema measurements %}
#### Response Example
{% include_example measurements %}

### POST
Creates new measurements.
#### Request Schema
{% include_schema measurement.create %}
#### Request Example
{% include_example measurement.create %}


# /rest/measurements/:measurement
### GET
Get the measurement `measurement`.
### Response Schema
{% include_schema measurement %}
### Response Example
{% include_example measurement %}

### DELETE
Deletes the measurement `measurement`.
