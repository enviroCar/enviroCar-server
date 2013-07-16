---
layout: default
---

# /rest/users/:username/activities

This is the activities of a user resource.

### GET 

Get a list of activities of the user `username`.

### Response Schema
{% include_schema activities %}

### Response Example
{% include_example activities %}


# /rest/users/:username/activities/:activity

This is the activity of acitvities of a user `username`.

### GET

Get a activity of the activities of a user `username`.

### Response Schema
{% include_schema activity %}

### Response Example
{% include_example activity %}

/rest/users/:username/activities/:activity
/rest/users/:username/friendActivities
/rest/users/:username/friendActivities/:activity
/rest/groups/:groupname/activities
/rest/groups/:groupname/activities/:activity