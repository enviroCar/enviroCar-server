---
layout: default
---

# /users/:username/activities

This is the activities of a user resource.

### GET 

Get a list of activities of the user `username`.

### Response Schema
{% include_schema activities %}

### Response Example
{% include_example activities %}


# /users/:username/activities/:activity

This is the activity of acitvities of a user `username`.

### GET

Get the activity `activity` of the activities of a user `username`.

### Response Schema
{% include_schema activity %}

### Response Example
{% include_example activity %}


# /users/:username/friendActivities

This is the fiend-activities of a user resource.

### GET

Get a list of acitvities of the friends of a user `username`.

### Response Schema
{% include_schema activities %}

### Response Example
{% include_example activities %}


# /users/:username/friendActivities/:activity

This is the activity of friend-acitvities of a user `username`.

### GET

Get the activity `activity` of the activities of a user `username`.

### Response Schema
{% include_schema activity %}

### Response Example
{% include_example activity %}


# /groups/:groupname/activities

This is the group-activities of a group resource.

### GET

Get a list of group-acitvities of a group `groupname`.

### Response Schema
{% include_schema activities %}

### Response Example
{% include_example activities %}


# /groups/:groupname/activities/:activity

This is the activity `activity` of group-acitvities of a group `groupname`.

### GET

Get the activity `activity` of the activities of a user `username`.

### Response Schema
{% include_schema activity %}

### Response Example
{% include_example activity %}
 
