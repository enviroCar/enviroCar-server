# car.io Server #

## Schema ##

The [JSON schema](http://json-schema.org/) is maintained in a [different git repository](https://github.com/car-io/car.io-schema).

## API Reference ##

*   `GET /rest`
    Get the root resource.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the root resource as specified in [root.json].

*   `GET /rest/users`

    Get a list of all users. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the user as specified in [users.json].

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

*   `GET /rest/users/:username`

    Get the user `username`.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the user as specified in [user.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).

*   `PUT /rest/users/:username`

    Updates the user `username`. Request schema: [user.modify.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the update was successfull.
    | `303 See Other`    | If the resource was moved as part of the update. The new URI is found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [user.modify.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).

* `DELETE /rest/users/:username`

    Deletes the user `username`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error. Further login attempts for the specified user will result in a `401 Unauthorized`.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user `username` (and not a admin).

## Deployments ##

* http://giv-car.uni-muenster.de:8080/dev/rest/
* http://giv-car.uni-muenster.de:8080/stable/rest/


[root.json]:        https://github.com/car-io/car.io-schema/blob/master/root.json "root.json"
[user.json]:        https://github.com/car-io/car.io-schema/blob/master/user.json "user.json"
[user.modify.json]: https://github.com/car-io/car.io-schema/blob/master/user.modify.json "user.modify.json"
[user.create.json]: https://github.com/car-io/car.io-schema/blob/master/user.create.json "user.create.json"
[users.json]:       https://github.com/car-io/car.io-schema/blob/master/users.json "users.json"
