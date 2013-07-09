---
layout: default
---

# /rest/users #

This is the Users resource.

### GET

Get a list of all users.

Query parameters:

| Name    | Type  | Default | Details
|---------|-------|---------|--------
| `limit` | `int` | `0`     | Limit the response to the `limit` newest users. A limit &le; 0 results in no limit.

#### Response Schema
{% include_schema users %}

#### Response Example

# /rest/users/:username #

### GET
Get the user `username`.

#### Response Schema
{% include_schema user %}

#### Response Example
{% include_example user %}

### POST
Creates a new user.

#### Request Schema
{% include_schema user.create %}

#### Response Example
none

### PUT
Updates the user `username`.

#### Request Schema
{% include_schema user.modify %}

#### Request Example
none

### DELETE ##
Deletes the user `username`.


