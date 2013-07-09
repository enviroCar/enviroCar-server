---
layout: default
---

This is the Friends resource.

# /rest/users/:username/friends/:friend

## Schema
{% include_schema friend %}
## Example
{% include_example friend %}

*   `GET /rest/users/:username/friends/:friend`

    Get a specific friend `friend` of the user `username`. 
	
*   `DELETE /rest/users/:username/friends/:friend`

    Removes a specific friend `friend` of the user `username`.
	
# /rest/users/:username/friends

## Schema
{% include_schema users %}
## Example
{% include_example users %}

*   `GET /rest/users/:username/friends`

    Get a list of friends of the user `username`.

*   `POST /rest/users/:username/friends`

    Adds a user as a friend to user `username`. 