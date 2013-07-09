---
layout: default
---

This is the Sensors resource.

# /rest/sensors/:sensorid

## Schema
{% include_schema sensor %}
## Example
{% include_example sensor %}

* `GET /rest/sensors/:sensor`

    Get a sensor by its id.

	
# /rest/sensors	

## Schema
{% include_schema sensors %}
	
*   `GET /rest/sensors`

    Get a list of all sensors. 

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest sensors. A limit &le; 0 results in no limit.

*   `POST /rest/sensors`

    Creates a new sensor.
