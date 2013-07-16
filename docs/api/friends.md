---
layout: default
---

# /rest/users/:username/friends
This is the Friends resource.

### GET
Get a list of friends of the user `username`.
#### Response Schema
{% include_schema users %}
#### Response Example
{% include_example users %}

### POST
Adds a user as a friend to user `username`.
#### Request Schema
{% include_example user.create %}

# /rest/users/:username/friends/:friend
### GET 
Get a specific friend `friend` of the user `username`. 

### Response Schema
{% include_schema user %}
### Response Example
{% include_example user %}
	
### DELETE
Removes a specific friend `friend` of the user `username`.
	
