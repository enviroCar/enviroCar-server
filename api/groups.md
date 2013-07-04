# /rest/groups

This is the groups resource.

*   `GET /rest/groups`

    Get a list of all groups. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the groups as specified in [groups.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

*   `POST /rest/groups`

    Creates a new group. Request schema: [group.create.json]. Responses:

    | Status             | Details
    |--------------------|--------
    | `201 Created`      | If the creation was successfull. The URI of the group can be found in the `Location` header field.
    | `400 Bad Request`  | If the request entity did not comply with [group.create.json].
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.

[group.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.json "group.json"
[group.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.modify.json "group.modify.json"
[group.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.create.json "group.create.json"
[groups.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/groups.json "groups.json"