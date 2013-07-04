--
layout: default
--

This is the Measurements of a Track resource.

*   `GET /rest/users/:username/tracks/:track/measurements`

    Get a list of all Measurements of the track `trackid`. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the measurements as specified in [measurements.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

*   `POST /rest/users/:username/tracks/:trackid/measurements`

    Creates a new measurements and adds it to the track `trackid`. Request schema: [measurement.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The URI of the measurement can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [measurement.create.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.

[measurement.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.json "measurement.json"
[measurements.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurements.json "measurements.json"
[measurement.create.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.create.json "measurement.create.json"
