---
layout: default
---
# API Reference
For Login/logout see [Authentification](authentification).

## Data Model
![datamodel][datamodel]

## Pagination

Every collection resource (e.g. `/rest/measurements`) offers two query parameters: `limit` and `page`. `limit` specifies the amount of retrieved entities while `page` indicates the position in the set of all entities. Per default `page` is 0 and `limit` will restrict the result to 100 entities. It is not possible to retrieve more than 100 entities at once. To allow navigation through the result set the service supports [RFC5988][rfc5988] conformant `Link` headers. The link relations used are quite self explanatory: `first`, `last`, `prev` and `next`.

```
> GET /rest/measurements?limit=2&page=20 HTTP/1.1
> Host: giv-car
> Accept: application/json
> 
< HTTP/1.1 200 OK
< [...]
< Link: <https://giv-car/rest/measurements?limit=2&page=1>;rel=first;type=application/json
< Link: <https://giv-car/rest/measurements?limit=2&page=19>;rel=prev;type=application/json
< Link: <https://giv-car/rest/measurements?limit=2&page=21>;rel=next;type=application/json
< Link: <https://giv-car/rest/measurements?limit=2&page=4169>;rel=last;type=application/json
< Content-Type: application/json; schema="http://schema.envirocar.org/measurements.json#"
[...]

```

Note that `next` and `prev` will not be present if they are equal to `first`/`last` or if they do not exist. `first` or `last` will not show up if you are already on the first or last page.

```
> GET /rest/measurements?limit=100&page=1 HTTP/1.1
> Host: giv-car
> Accept: application/json
> 
< HTTP/1.1 200 OK
< [...]
< Link: <https://giv-car/rest/measurements?limit=100&page=84>;rel=last;type=application/json
< Link: <https://giv-car/rest/measurements?limit=100&page=2>;rel=next;type=application/json
< Content-Type: application/json; schema="http://schema.envirocar.org/measurements.json#"
[...]
```

Please also note that the content of the pages will change over time and the insertions, deletion or other changes in the sort order can result in duplicate or missing received entities.

## Endpoints

* [Root](root)
* [Friends](friends)
    * `/rest/users/:username/friends`
    * `/rest/users/:username/friends/:friend`
* [Tracks](tracks)
    * `/rest/tracks`
    * `/rest/tracks/:trackid`
    * `/rest/users/:username/tracks`
    * `/rest/users/:username/tracks/:trackid`
* [Phenomenons](phenomenons)
    * `/rest/phenmenons`
    * `/rest/phenmenons/:phenomenon`
* [Sensors](sensors)
    * `/rest/sensors`
    * `/rest/sensors/:sensor`
* [Groups](groups)
    * `/rest/groups`
    * `/rest/groups/:groupname`
    * `/rest/users/:username/groups`
    * `/rest/users/:username/groups/:groupname`
    * `/rest/groups/:groupname/members`
    * `/rest/groups/:groupname/members/:username`
* [Users](users)
    * `/rest/users`
    * `/rest/users/:username`
* [Measurements](measurements)
    * `/rest/measurements`
    * `/rest/measurements/:measurementid`
    * `/rest/tracks/:trackid/measurements`
    * `/rest/tracks/:trackid/measurements/:measurementid`
    * `/rest/users/:username/measurements`
    * `/rest/users/:username/measurements/:measurementid`
    * `/rest/users/:username/tracks/:trackid/measurements`
    * `/rest/users/:username/tracks/:trackid/measurements/:measurementid`
* [Statistics](statistics)
    * `/rest/statistics`
    * `/rest/statistics/:phenomenon`
    * `/rest/sensors/:sensor/statistics`
    * `/rest/sensors/:sensor/statistics/:phenomenon`
    * `/rest/tracks/:trackid/statistics`
    * `/rest/tracks/:trackid/statistics/:phenomenon`
    * `/rest/users/:username/statistics`
    * `/rest/users/:username/statistics/:phenomenon`
* [Activities](activities)
    * `/rest/users/:username/activities`
    * `/rest/users/:username/activities/:activity`
    * `/rest/users/:username/friendActivities`
    * `/rest/users/:username/friendActivities/:activity`
    * `/rest/groups/:groupname/activities`
    * `/rest/groups/:groupname/activities/:activity`

[datamodel]: {{site.url}}/images/datamodel.png "Data model"
[rfc5988]: http://tools.ietf.org/html/rfc5988 "RFC 5988: Web Linking"