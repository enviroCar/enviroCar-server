---
layout: default
---

# /rest/users #
### GET
Get a list of all users.
#### Response Schema
{% include_schema users %}
#### Response Example
{% include_example users %}

# /rest/users/:user #
### GET
Get the user `user`.
#### Response Schema
{% include_schema user %}
#### Response Example
{% include_example user %}

### POST
Creates a new user.
#### Request Schema
{% include_schema user.create %}
#### Request Example
{% include_example user.create %}

### PUT
Updates the user `user`.
#### Request Schema
{% include_schema user.modify %}
#### Request Example
{% include_example user.modify %}

### DELETE ##
Deletes the user `user`.


