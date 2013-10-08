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

In addition to the standard query parameters, measurements can be queried by a given boundingbox. Therefore you have to add the following get parameter: `bbox=minx,miny,maxx,maxy`.
(example: `/rest/measurements?bbox=7.0,51.1,7.3,52.0`)

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
