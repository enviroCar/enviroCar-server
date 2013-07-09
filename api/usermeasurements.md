---
layout: default
---

This is the Measurements of a User(-track) resource.

# /rest/users/:username/measurements/:measurementid

Also available under `/rest/users/:username/tracks/:trackid/measurements/:measurementid`

## Schema
{% include_schema measurement %}
## Example
{% include_example measurement %}

*   `GET /rest/users/:username/measurements/:measurementid` OR 
	`GET /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Get the measurement `measurementid` of a track `trackid` of a specific user `username`.

*   `PUT /rest/users/:username/measurements/:measurementid` OR
	`PUT /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Updates the measurement `measurementid` of track `trackid` from user `username`. 

*	`DELETE /rest/users/:username/measurements/:measurementid` OR
	`DELETE /rest/users/:username/tracks/:trackid/measurements/:measurementid`

    Deletes the measurement `measurementid` of track `trackid` from user `username`.
	
	
# /rest/users/:username/measurements

Also available under `/rest/users/:username/tracks/:trackid/measurements`

## Schema
{% include_schema measurements %}
## Example
{% include_example measurements %}

*   `GET /rest/users/:username/measurements` OR
	`GET /rest/users/:username/tracks/:trackid/measurements`

    Get a list of all Measurements of the track `trackid`.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

*   `POST /rest/users/:username/measurements` OR
	`POST /rest/users/:username/tracks/:trackid/measurements`

    Creates a new measurements and adds it to the track `trackid`.