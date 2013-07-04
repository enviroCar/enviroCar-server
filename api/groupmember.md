# /rest/groups/:groupname/members/:username

*   `GET /rest/groups/:groupname/members/:username`

    Get the member `username` of the group `groupname`.

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the group as specified in [group.json].
    | `404 Not Found`    | If the group `groupname` does not exist.


* `DELETE /rest/groups/:groupname/members/:username`

    Removes the user `username` from the group `groupname`. Responses:

    | Status             | Details
    |--------------------|--------
    | `204 No Content`   | If the deletion was successfull. All subsequent request to the resource will result in an error.
    | `401 Unauthorized` | If the user did not send credentials or the credentials are invalid.
    | `403 Forbidden`    | If the authenticated user is not the user that owns the group (and not a admin).
    | `404 Not Found`    | If the member `username` of group `groupname` does not exist.


[group.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.json "group.json"
[group.modify.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.modify.json "group.modify.json"
[group.create.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/group.create.json "group.create.json"
[groups.json]:       https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/groups.json "groups.json"