---
layout: default
---
# /rest/statistics
### GET
Get a list of statistics for all phenomenons.
#### Response Schema
{% include_schema statistics %}
#### Response Schema
{% include_example statistics %}


# /rest/statistics/:phenomenon
### GET
Get the statistics for the specified `phenomenon`.
#### Response Schema
{% include_schema statistic %}
#### Response Schema
{% include_example statistic %}