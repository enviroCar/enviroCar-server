* `GET /rest/phenomenons/:phenomenon`

    Gets a phenomenon. Responses:
    
    | Status             | Details
    |--------------------|--------
    | `200 OK`           | Gets the phenomenon `phenomenon` with a schema described by [phenomenon.json].
    | `404 Not Found`    | If the `phenomenon` of `phenomenons` does not exist.


[phenomenon.json]: https://github.com/enviroCar/enviroCar-server/blob/master/rest/src/main/resources/schema/phenomenon.json "phenomenon.json"

* Example [phenomenon.json] file:

    ```json
    {
        "name": "name",
        "created": "2013-01-01T10:01:20+02",
        "modified": "2013-01-01T10:01:20+02"
    }
    ```