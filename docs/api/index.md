---
layout: default
---
# API Reference
For Login/logout see [Authentification](authentification).

## Data Model
![datamodel][datamodel]

## Pagination

Every collection resource (e.g. `/measurements`) offers two query parameters: `limit` and `page`. `limit` specifies the amount of retrieved entities while `page` indicates the position in the set of all entities. Per default `page` is 0 and `limit` will restrict the result to 100 entities. It is not possible to retrieve more than 100 entities at once. To allow navigation through the result set the service supports [RFC5988][rfc5988] conformant `Link` headers. The link relations used are quite self explanatory: `first`, `last`, `prev` and `next`.

```
> GET /api/stable/measurements?limit=2&page=20 HTTP/1.1
> Host: envirocar.org
> Accept: application/json
> 
< HTTP/1.1 200 OK
< [...]
< Link: <https://envirocar.org/api/stable/measurements?limit=2&page=1>;rel=first;type=application/json
< Link: <https://envirocar.org/api/stable/measurements?limit=2&page=19>;rel=prev;type=application/json
< Link: <https://envirocar.org/api/stable/measurements?limit=2&page=21>;rel=next;type=application/json
< Link: <https://envirocar.org/api/stable/measurements?limit=2&page=4169>;rel=last;type=application/json
< Content-Type: application/json; schema="https://envirocar.org/api/stable/schema/measurements.json#"
[...]

```

Note that `next` and `prev` will not be present if they are equal to `first`/`last` or if they do not exist. `first` or `last` will not show up if you are already on the first or last page.

```
> GET /api/stable/measurements?limit=100&page=1 HTTP/1.1
> Host: envirocar.org
> Accept: application/json
> 
< HTTP/1.1 200 OK
< [...]
< Link: <https://envirocar.org/api/stable/measurements?limit=100&page=84>;rel=last;type=application/json
< Link: <https://envirocar.org/api/stable/measurements?limit=100&page=2>;rel=next;type=application/json
< Content-Type: application/json; schema="https://envirocar.org/api/stable/schema/measurements.json#"
[...]
```

Please also note that the content of the pages will change over time and the insertions, deletion or other changes in the sort order can result in duplicate or missing received entities.

## Endpoints

The current API endpoint is: [https://envirocar.org/api/stable](https://envirocar.org/api/stable).

The following resources are located relatively to the API endpoint:

* [Root](root)
* [Friends](friends)
    * `/users/:username/friends`
    * `/users/:username/friends/:friend`
* [Tracks](tracks)
    * `/tracks`
    * `/tracks/:trackid`
    * `/users/:username/tracks`
    * `/users/:username/tracks/:trackid`
* [Phenomenons](phenomenons)
    * `/phenmenons`
    * `/phenmenons/:phenomenon`
* [Sensors](sensors)
    * `/sensors`
    * `/sensors/:sensor`
* [Groups](groups)
    * `/groups`
    * `/groups/:groupname`
    * `/users/:username/groups`
    * `/users/:username/groups/:groupname`
    * `/groups/:groupname/members`
    * `/groups/:groupname/members/:username`
* [Users](users)
    * `/users`
    * `/users/:username`
* [Measurements](measurements)
    * `/measurements`
    * `/measurements/:measurementid`
    * `/tracks/:trackid/measurements`
    * `/tracks/:trackid/measurements/:measurementid`
    * `/users/:username/measurements`
    * `/users/:username/measurements/:measurementid`
    * `/users/:username/tracks/:trackid/measurements`
    * `/users/:username/tracks/:trackid/measurements/:measurementid`
* [Statistics](statistics)
    * `/statistics`
    * `/statistics/:phenomenon`
    * `/sensors/:sensor/statistics`
    * `/sensors/:sensor/statistics/:phenomenon`
    * `/tracks/:trackid/statistics`
    * `/tracks/:trackid/statistics/:phenomenon`
    * `/users/:username/statistics`
    * `/users/:username/statistics/:phenomenon`
* [Activities](activities)
    * `/users/:username/activities`
    * `/users/:username/activities/:activity`
    * `/users/:username/friendActivities`
    * `/users/:username/friendActivities/:activity`
    * `/groups/:groupname/activities`
    * `/groups/:groupname/activities/:activity`

[datamodel]: {{site.url}}/images/datamodel.png "Data model"
[rfc5988]: http://tools.ietf.org/html/rfc5988 "RFC 5988: Web Linking"
