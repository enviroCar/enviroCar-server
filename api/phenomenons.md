---
layout: default
---

This is the Phenomenons resource.

*   `GET /rest/phenomenons`

    Get a list of all phenomenons. Responses:

    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Returns the phenomenons as specified in [phenomenons.json].

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest phenomenons. A limit &le; 0 results in no limit.



[phenomenon.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/phenomenon.json "phenomenon.json"
[phenomenons.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/phenomenons.json "phenomenons.json"
[phenomenon.create.json]:        https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/phenomenons.json "phenomenon.create.json"
