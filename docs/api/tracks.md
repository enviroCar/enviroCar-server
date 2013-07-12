---
layout: default
---

# /rest/tracks #

This is the Tracks resource.

### GET

Get a list of all tracks.

Query parameters:

| Name    | Type  | Default | Details
|---------|-------|---------|--------
| `limit` | `int` | `0`     | Limit the response to the `limit` newest tracks. A limit &le; 0 results in no limit.


### Response Schema
{% include_schema tracks %}

### Response Example

### POST

Creates a new tracks and adds current user as the owner.

### Request Schema
{% include_schema track.create %}

# /rest/tracks/:trackid #

### GET

Get a track.

### Response Schema
{% include_schema track %}

### Response Example
{% include_example track %}

### PUT

Modify a track.

### Request Schema
{% include_schema track.create %}
	

