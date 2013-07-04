--
layout: default
--

# /rest/users/:username/tracks/:trackid

*   `GET /rest/users/:username/tracks/:trackid`

    Get the track `trackid` of a user.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the track as specified in [track.json].
    | `404 Not Found`    | If the track `trackid` of user `username` does not exist.

*   `PUT /rest/users/:username/groups/:groupname`

    Updates the track `trackid` of user `username`. Request schema: [track.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [track.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the track (and not a admin).
    | `404 Not Found`    | If the track `trackid` of user `username` does not exist.

* `DELETE /rest/users/:username/tracks/:trackid`

    Deletes the track `trackid` from user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the track (and not a admin).
    | `404 Not Found`    | If the track `trackid` of user `username` does not exist.


[track.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/track.json "track.json"
[track.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/track.modify.json "track.modify.json"
[track.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/track.create.json "track.create.json"
[tracks.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/tracks.json "tracks.json"