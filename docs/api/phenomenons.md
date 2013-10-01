---
layout: default
---

This is the Phenomenons resource.

# /rest/phenomenons/:name
### GET
Get the phenomenon with the name `name`.
#### Response Schema
{% include_schema phenomenon %}
#### Response Example
{% include_example phenomenon %}

# /rest/phenomenons
### GET
Get a list of all phenomenons.
#### Schema
{% include_schema phenomenons %}
#### Example
{% include_example phenomenons %}

### POST
Create a new phenomenon
#### Schema
{% include_schema phenomenon.create %}
#### Example
{% include_example phenomenon.create %}