---
layout: default
---

# /rest/tracks #

### GET
Get a list of all tracks.
#### Response Schema
{% include_schema tracks %}
#### Response Example
{% include_example tracks %}

### POST
Creates a new tracks and adds current user as the owner.
#### Request Schema
{% include_schema track.create %}
#### Request Example
{% include_example track.create %}

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
### Request Example
{% include_example track.create %}
	

