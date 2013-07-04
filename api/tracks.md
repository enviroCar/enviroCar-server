This is the Tracks resource.

*   `GET /rest/tracks`

    Get a list of all tracks. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the tracks as specified in [tracks.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest tracks. A limit &le; 0 results in no limit.

*   `POST /rest/tracks`

    Creates a new tracks and adds current user as the owner. Request schema: [track.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The URI of the track can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [track.create.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.

[track.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/track.json "track.json"
[tracks.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/tracks.json "tracks.json"
[track.create.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/tracks.json "track.create.json"
