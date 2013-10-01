---
layout: default
---

This is the groups resource.

# /rest/groups #
### GET
Get a list of all groups.
#### Response Schema
{% include_schema groups %}
#### Response Example
{% include_example groups %}

# /rest/groups/:group
### GET
Get the group with the name `group`.
#### Response Schema
{% include_schema group %}
#### Response Example
{% include_example groups %}

### PUT
Updates the group `group`.
#### Request Schema
{% include_schema group.modify %}
#### Request Example
{% include_example group.modify %}

### DELETE
Deletes the group `group`. Just the owner of the group is able to delete the group.

# /rest/groups/:group/members
### GET
Get a list of members of the group `group`.
#### Response Schema
{% include_schema users %}
#### Response Example
{% include_example users %}

### POST
Adds a user to the group `group`.
#### Request Schema
{% include_schema user.ref %}
#### Request Example
{% include_example user.ref %}


# /rest/groups/:group/members/:member
Get the member `member` of the group `group`.
### GET
Get the member `member` of the group `group`.
#### Response Schema
{% include_schema user %}
#### Response Example
{% include_example user %}

### DELETE
Removes the user `member` from the group `group`.

