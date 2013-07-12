---
layout: default
---

This is the groups resource.

# /rest/groups #

### GET

Get a list of all groups.

{% include pagination %}

#### Response Schema
{% include_schema groups %}

#### Response Example
{% include_example groups %}



# /rest/groups/:groupname

### GET
Get the group `groupname`.

#### Response Schema
{% include_schema group %}

#### Response Example
{% include_example groups %}


### PUT
Updates the group `groupname`.

#### Request Schema
{% include_schema group.modify %}

#### Response Example
none


### DELETE
Deletes the group `groupname`. Just the owner of the group is able to delete the group.


# /rest/groups/:groupname/members

### GET
Get a list of members of the group `groupname`.

#### Response Schema
{% include_schema users %}

#### Response Example
...


### POST
Adds a user to the group `groupname`.

#### Request Schema
{% include_schema user.ref %}

#### Request Example
{% include_example user.ref %}


# /rest/groups/:groupname/members/:username
Get the member `username` of the group `groupname`.

### GET
Get the member `username` of the group `groupname`.

#### Response Schema
{% include_schema user %}

#### Response Example
{% include_example user %}


### DELETE
Removes the user `username` from the group `groupname`.

