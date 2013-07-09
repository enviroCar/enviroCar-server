---
layout: default
---

# /rest/groups/:groupname/members/:username

## Schema
{% include_schema group %}
## Example
{% include_example group %}

*   `GET /rest/groups/:groupname/members/:username`

    Get the member `username` of the group `groupname`.

*	`DELETE /rest/groups/:groupname/members/:username`

    Removes the user `username` from the group `groupname`.
	
	
# /rest/groups/:groupname/members

## Schema
{% include_schema users %}

*   `GET /rest/groups/:groupname/members`

    Get a list of members of the group `groupname`.

*   `POST /rest/groups/:groupname/members`

    Adds a user to the group `groupname`.