---
layout: default
---
# /rest/sensors #
### GET
Get a list of all sensors.
#### Response Schema
{% include_schema sensor %}
#### Response Example
{% include_example groups %}

# /rest/sensors/:sensor #
### GET
Get the sensor `sensor`.
#### Response Schema
{% include_schema sensor %}
#### Response Example
{% include_example sensor %}

### POST
Creates a new sensor.
#### Request Schema
{% include_schema sensor.create %}
#### Request Example
{% include_example sensor.create %}

