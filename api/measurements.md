---
layout: default
---

# /rest/measurements

This is the measurements resource.

*   `GET /rest/measurements`

    Get a list of all measurements. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the measurements as specified in [measurements.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

*   `POST /rest/measurements`

    Creates a new user. Request schema: [measurement.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The uri of the measurement can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [measurement.create.json].
    | `403 Forbidden`    | If the request contained credentials.

[root.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/root.json "root.json"
[measurement.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.json "measurement.json"
[measurement.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.modify.json "measurement.modify.json"
[measurement.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.create.json "measurement.create.json"
[measurements.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurements.json "measurements.json"