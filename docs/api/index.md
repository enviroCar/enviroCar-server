---
layout: default
---
# API Reference
For Login/logout see [Authentification](authentification).

## Data Model
![datamodel][datamodel]

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
