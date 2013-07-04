--
layout: default
--

* `GET /rest/sensors/:sensor`

    Get a sensor. Responses:
    
    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Gets the sensor `sensor` with a schema described by [sensor.json].
    | `404 Not Found`    | If the `sensor` of `sensors` does not exist.


[sensor.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/sensor.json "sensor.json"

* Example [sensor.json] file:

    ```json
    {
        "name": "name",
        "created": "2013-01-01T10:01:20+02",
        "modified": "2013-01-01T10:01:20+02"
    }
    ```