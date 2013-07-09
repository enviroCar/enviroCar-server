---
layout: default
---

This is the groups resource.

# /rest/groups/:groupname

## Schema
{% include_schema group %}
## Example
{% include_example group %}

*   `GET /rest/groups/:groupname`

    Get the group `groupname`.

*   `PUT /rest/groups/:groupname`

    Updates the group `groupname`.

* `DELETE /rest/groups/:groupname`

    Deletes the group `groupname`.


# /rest/groups

## Schema
{% include_schema groups %}

*   `GET /rest/groups`

    Get a list of all groups.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

*   `POST /rest/groups`

    Creates a new group.



# /rest/groups/:groupname/members/:username

## Schema
{% include_schema group %}
## Example
{% include_example group %}

*   `GET /rest/groups/:groupname/members/:username`

    Get the member `username` of the group `groupname`.

*   `DELETE /rest/groups/:groupname/members/:username`

    Removes the user `username` from the group `groupname`.


# /rest/groups/:groupname/members

## Schema
{% include_schema users %}

*   `GET /rest/groups/:groupname/members`

    Get a list of members of the group `groupname`.

*   `POST /rest/groups/:groupname/members`

    Adds a user to the group `groupname`.
