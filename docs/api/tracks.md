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

In addition to the standard query parameters, tracks can be queried by a given boundingbox. Therefore you have to add the following get parameter: `bbox=minx,miny,maxx,maxy`.
(example: `/rest/tracks?bbox=7.0,51.1,7.3,52.0`)

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
