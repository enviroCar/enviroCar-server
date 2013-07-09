---
layout: default
---
	
This is the Tracks of a User resource.

# /rest/users/:username/tracks/:trackid

## Schema
{% include_schema track %}
## Example
{% include_example track %}

*   `GET /rest/users/:username/tracks/:trackid`

    Get the track `trackid` of a user.

*   `PUT /rest/users/:username/groups/:groupname`

    Updates the track `trackid` of user `username`.

* `DELETE /rest/users/:username/tracks/:trackid`

    Deletes the track `trackid` from user `username`. 
	
	
# /rest/users/:username/tracks

## Schema
{% include_schema tracks %}

*   `GET /rest/users/:username/tracks`

    Get a list of all tracks of the user `username`. 

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest tracks. A limit &le; 0 results in no limit.

*   `POST /rest/users/:username/tracks`

    Creates a new tracks and adds the user `username` as the owner.
