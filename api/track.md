---
layout: default
---

*   `GET /rest/tracks/:track`

    Get a track. Responses:
    
    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Gets the track `track` with a schema described by [track.json].
    | `404 Not Found`    | If the `track` of `tracks` does not exist.

[track.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/track.json "track.json"