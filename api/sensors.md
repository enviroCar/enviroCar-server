---
layout: default
---

This is the Sensors resource.

*   `GET /rest/sensors`

    Get a list of all sensors. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the sensors as specified in [sensors.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest sensors. A limit &le; 0 results in no limit.

*   `POST /rest/sensors`

    Creates a new sensor. Request schema: [sensors.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The URI of the sensor can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [sensor.create.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.

[sensor.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/sensor.json "sensor.json"
[sensors.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/sensors.json "sensors.json"
[sensor.create.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/sensors.json "sensor.create.json"
