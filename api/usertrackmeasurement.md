--
layout: default
--

# /rest/users/:username/tracks/:trackid/measurements/:measurementid

*   `GET /rest/users/:username/tracks/:trackid/measurements/:mesaurementid`

    Get the measurement `measurementid` of a track.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the measurement as specified in [measurement.json].
    | `404 Not Found`    | If the measurement `measurementid` of track `trackid` does not exist.

*   `PUT /rest/users/:username/tracks/:trackname/measurements/:mesaurementid`

    Updates the measurement `measurementid`. Request schema: [measurement.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [track.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the track (and not a admin).
    | `404 Not Found`    | If the track `trackid` of user `username` does not exist.

* `DELETE /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Deletes the measurement `measurementid` from track `trackid`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the measurement(and not a admin).
    | `404 Not Found`    | If the measurement `measurementid` of track `trackid` does not exist.


[measurement.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.json "measurement.json"
[measurement.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.modify.json "mesaurement.modify.json"
[measurement.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurement.create.json "measurement.create.json"
[measurements.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/measurements.json "measurements.json"