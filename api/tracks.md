---
layout: default
---

This is the Tracks resource.

# /rest/tracks/:trackid

## Schema
{% include_schema track %}
## Example
{% include_example track %}

*   `GET /rest/tracks/:track`

    Get a track.

	
# /rest/tracks

## Schema
{% include_schema tracks %}
## Example
{% include_example tracks %}

*   `GET /rest/tracks`

    Get a list of all tracks.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest tracks. A limit &le; 0 results in no limit.

*   `POST /rest/tracks`

    Creates a new tracks and adds current user as the owner.
