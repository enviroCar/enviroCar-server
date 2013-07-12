---
layout: default
---

This is the Phenomenons resource.

# /rest/phenomenons/:name

## Schema
{% include_schema phenomenon %}
## Example
{% include_example phenomenon %}

* `GET /rest/phenomenons/:phenomenon`

    Gets a phenomenon.

# /rest/phenomenons

## Schema
{% include_schema phenomenons %}
## Example
{% include_example phenomenons %}

*   `GET /rest/phenomenons`

    Get a list of all phenomenons.

    Query parameters:

    | Name    | Type  | Default | Details
    |---------|-------|---------|--------
    | `limit` | `int` | `0`     | Limit the response to the `limit` newest phenomenons. A limit &le; 0 results in no limit.
