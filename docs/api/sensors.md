---
layout: default
---

This is the Sensors resource.

# /rest/sensors #

### GET

Get a list of all sensors.

{% include pagination %}

#### Response Schema
{% include_schema sensor %}

#### Response Example
{% include_example groups %}

# /rest/sensors/:sensorid #

### GET
Get the sensor `sensorid`.

#### Response Schema
{% include_schema sensor %}

#### Response Example
{% include_example sensor %}

### POST
Creates a new sensor.

#### Request Schema
{% include_schema sensor.create %}

#### Response Example
none

