---
layout: default
---

This is the Users resource.

# /rest/users/:username

## Schema
{% include_schema user %}
## Example
{% include_example user %}

*   `GET /rest/users/:username`

    Get the user `username`.
	
*   `PUT /rest/users/:username`

    Updates the user `username`. Request schema: [user.modify.json].
	
*	`DELETE /rest/users/:username`

    Deletes the user `username`.

	
# /rest/users

## Schema
{% include_schema users %}

*   `GET /rest/users`

    Get a list of all users.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

*   `POST /rest/users`

    Creates a new user.
