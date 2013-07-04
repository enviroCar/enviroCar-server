--
layout: default
--

# /rest/users

This is the users resource.

*   `GET /rest/users`

    Get a list of all users. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the users as specified in [users.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

*   `POST /rest/users`

    Creates a new user. Request schema: [user.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The uri of the user can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [user.create.json].
    | `403 Forbidden`    | If the request contained credentials.

[root.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/root.json "root.json"
[user.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.json "user.json"
[user.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.modify.json "user.modify.json"
[user.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.create.json "user.create.json"
[user.ref.json]:    https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/user.ref.json "user.ref.json"
[users.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/users.json "users.json"