---
layout: default
---

# /rest/users/:username/friends/:friend

*   `GET /rest/users/:username/friends/:friend`

    Get a specific friend `friend` of the user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the friends as specified in [users.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).
    | `404 Not Found`    | If the user `username` does not exist.

*   `DELETE /rest/users/:username/friends/:friend`

    Removes a specific friend `friend` of the user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | Returns the friends was removed.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).
    | `404 Not Found`    | If the user `username` or `friend` does not exist.


[root.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/root.json "root.json"
[user.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.json "user.json"
[user.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.modify.json "user.modify.json"
[user.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.create.json "user.create.json"
[user.ref.json]:    https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.ref.json "user.ref.json"
[users.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/users.json "users.json"