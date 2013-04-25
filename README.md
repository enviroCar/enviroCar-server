# car.io Server #

## Schema ##

The [JSON schema](http://json-schema.org/) is maintained in a [different git repository](https://github.com/car-io/car.io-schema).

## Endpoints ##

*   `/rest`:

    | Method   | Request            | Response     |
    |:--------:|:-------------------|:-------------|
    | `GET`    |                    | [root.json]  |

*   `/rest/users`:

    | Method   | Request            | Response               |
    |:--------:|:-------------------|:-----------------------|
    | `GET`    |                    | `200 OK` [users.json]  |
    | `POST`   | [user.create.json] | `201 Created`          |

*   `/rest/users/:username`:

    | Method   | Request            | Response                    |
    |:--------:|:-------------------|:----------------------------|
    | `GET`    |                    | `200 OK` [user.json]        |
    | `PUT`    | [user.modify.json] | `200 OK` or `303 See Other` |
    | `DELETE` |                    | `204 No Content`            |

## Deployments ##

* http://giv-car.uni-muenster.de:8080/dev/rest/


[root.json]:        https://github.com/car-io/car.io-schema/blob/master/root.json "root.json"
[user.json]:        https://github.com/car-io/car.io-schema/blob/master/user.json "user.json"
[user.modify.json]: https://github.com/car-io/car.io-schema/blob/master/user.modify.json "user.modify.json"
[user.create.json]: https://github.com/car-io/car.io-schema/blob/master/user.create.json "user.create.json"
[users.json]:       https://github.com/car-io/car.io-schema/blob/master/users.json "users.json"
