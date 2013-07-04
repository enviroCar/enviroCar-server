# /rest/users/:username/groups/:groupname

*   `GET /rest/users/:username/groups/:groupname`

    Get the group `groupname`.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the group as specified in [group.json].
    | `404 Not Found`    | If the group `groupname` does not exist.

*   `PUT /rest/users/:username/groups/:groupname`

    Updates the group `groupname`. Request schema: [group.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [group.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the group (and not a admin).
    | `404 Not Found`    | If the group `groupname` does not exist.

* `DELETE /rest/users/:username/groups/:groupname`

    Deletes the group `groupname`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the group (and not a admin).
    | `404 Not Found`    | If the group `groupname` does not exist.


[group.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.json "group.json"
[group.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.modify.json "group.modify.json"
[group.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.create.json "group.create.json"
[groups.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/groups.json "groups.json"