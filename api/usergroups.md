---
layout: default
---

This is the Groups of a User resource.

# /rest/users/:username/groups/:groupname

## Schema
{% include_schema group %}
## Example
{% include_example group %}

*   `GET /rest/users/:username/groups/:groupname`

    Get the group `groupname`.

*   `PUT /rest/users/:username/groups/:groupname`

    Updates the group `groupname`. 

* `DELETE /rest/users/:username/groups/:groupname`

    Deletes the group `groupname`.
	
	
# /rest/users/:username/groups

## Schema
{% include_schema groups %}


*   `GET /rest/users/:username/groups`

    Get a list of all groups of the user `username`.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

*   `POST /rest/users/:username/groups`

    Creates a new group and adds the user `username` as a member.