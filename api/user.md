# /rest/users/:username

*   `GET /rest/users/:username`

    Get the user `username`.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the user as specified in [user.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).
    | `404 Not Found`    | If the user `username` does not exist.

*   `PUT /rest/users/:username`

    Updates the user `username`. Request schema: [user.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [user.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).
    | `404 Not Found`    | If the user `username` does not exist.

* `DELETE /rest/users/:username`

    Deletes the user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error. Further login attempts for the specified user will result in a `401 Unauthorized`.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).
    | `404 Not Found`    | If the user `username` does not exist.


[root.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/root.json "root.json"
[user.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.json "user.json"
[user.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.modify.json "user.modify.json"
[user.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.create.json "user.create.json"
[user.ref.json]:    https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.ref.json "user.ref.json"
[users.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/users.json "users.json"