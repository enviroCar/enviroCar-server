---
layout: default
---

This is the Measurements of a Track resource.

# /rest/tracks/:trackid/measurements/:measurementid

## Schema
{% include_schema measurement %}
## Example
{% include_example measurement %}

*   `GET /rest/tracks/:trackid/measurements/:measurementid`

    Get the measurement `measurementid` of a track `trackid`.

*   `PUT /rest/tracks/:trackid/measurements/:measurementid`

    Updates the measurement `measurementid` of track `trackid`.

* `DELETE /rest/tracks/:trackid/measurements/:measurementid`

    Deletes the measurement `measurementid` of track `trackid`.
	
	
# /rest/tracks/:trackid/measurements/:measurementid

## Schema
{% include_schema measurements %}
## Example
{% include_example measurements %}

*   `GET /rest/tracks/:track/measurements`

    Get a list of all Measurements of the track `trackid`.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest measurements. A limit &le; 0 results in no limit.

*   `POST /rest/tracks/:trackid/measurements`

    Creates a new measurements and adds it to the track `trackid`.
