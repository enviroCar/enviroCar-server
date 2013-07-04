--
layout: default
--

# /rest/groups/:groupname/members

*   `GET /rest/groups/:groupname/members`

    Get a list of members of the group `groupname`. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the members as specified in [users.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the user is not authenticated (and not a admin).
    | `404 Not Found`    | If the group `groupname` does not exist.

*   `POST /rest/groups/:groupname/members`

    Adds a user described by [user.ref.json] to the group `groupname`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the member was added.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the user is not authenticated (and not a admin).
    | `404 Not Found`    | If the group`groupname` does not exist.


[root.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/root.json "root.json"
[group.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.json "user.json"
[group.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.modify.json "group.modify.json"
[group.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.create.json "group.create.json"
[user.ref.json]:    https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.ref.json "user.ref.json"
[group.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/groups.json "groups.json"