# /rest/users/:username/tracks/:trackid/measurements/:measurementid

*   `GET /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Get the measurement `measurementid` of a track `trackid` of a specific user `username`.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the measurement as specified in [measurement.json].
    | `404 Not Found`    | If the measurement `measurementid` of track `trackid` from user `username` does not exist.

*   `PUT /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Updates the measurement `measurementid` of track `trackid` from user `username`. Request schema: [measurement.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [measurement.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the measurement (and not a admin).
    | `404 Not Found`    | If the measurement `measurementid` of track `trackid` from user `username` does not exist.

* `DELETE /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Deletes the measurement `measurementid` of track `trackid` from user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the track (and not a admin).
    | `404 Not Found`    | If the track `trackid` of user `username` does not exist.


[measurement.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.json "measurement.json"
[measurement.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.modify.json "measurement.modify.json"
[measurement.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.create.json "mesaurement.create.json"
[measurements.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurements.json "mesaurements.json"