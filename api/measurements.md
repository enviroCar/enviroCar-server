---
layout: default
---

This is the measurements resource.

# /rest/measurements/:measurementid

## Schema
{% include_schema measurement %}
## Example
{% include_example measurement %}

*   `GET /rest/measurements/:measurementid`

    Get the measurement `measurementid`.

*   `PUT /rest/measurements/:measurementid`

    Updates the measurement `measurementid`. Request schema: [measurement.modify.json].

*	`DELETE /rest/measurements/:measurementid`

    Deletes the measurement `measurementid`.
	
# /rest/measurements

## Schema
{% include_schema measurements %}
## Example
{% include_example measurements %}

*   `GET /rest/measurements`

    Get a list of all measurements. 

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

*   `POST /rest/measurements`

    Creates a new user. Request schema: [measurement.create.json]. 