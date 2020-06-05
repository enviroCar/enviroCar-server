# enviroCar Server [![Build Status](https://travis-ci.org/enviroCar/enviroCar-server.png)](https://travis-ci.org/enviroCar/enviroCar-server) #

## Description

### User and Data Management

**Managing and sharing Open XFCD**

The enviroCar server’s central tasks are the enviroCar platform user management and
the management of the XFCD data provided as OpenData by the enviroCar drivers. To
ensure data privacy, the server anonymizes the recorded tracks for external access.
The enviroCar server’s RESTful API provides access to driving statistics, as well as space
and time related subsets of the XFCD data in various formats.

52°North reengineered user management workflows for GDPR compliance, dockerized
all components, created new data analysis features (e.g. OSM MapMatching) and implemented
the publication of newly created tracks via Apache Kafka. In addition, we developed
an anonymous API that allows the upload of tracks without a user account.

**Key Technologies**

- MongoDB
- Apache Kafka
- Java
- Guice
- Guava

**Benefits**

- User management support
- XFCD data management
- Open API for data export: JSON, CSV, SHP
- Linked Data/RDF API
- Publishing anonymized tracks via Apache Kafka
- GDPR compliance

## Schema ##

The [JSON Schema](http://json-schema.org/) is maintained in [`rest/src/main/resources/schema`](rest/src/main/resources/schema).

## API Reference ##

See [envirocar.github.io/enviroCar-server](http://envirocar.github.io/enviroCar-server/api/).

The documentation is a GitHub page and managed in the branch ``gh-pages``, see https://github.com/enviroCar/enviroCar-server/tree/gh-pages.

## Deployments ##

* **Stable API**: https://envirocar.org/api/stable/ (the master branch)
* **Development API**: https://envirocar.org/api/dev/ (the dev branch)

## Quick Start 

### Building & Installation ###

* Install and run [MongoDB](http://www.mongodb.org/downloads) >2.4.
* Clone the repository and switch to the directory.
* Configure the connection in `mongo/src/main/resources/mongo.properties` (if needed).
* Run `mvn clean install`.
* Deploy the `war` file in `webapp/target` to a application server of your choice (e.g. [Apache Tomcat](http://tomcat.apache.org/)).

## References
The following project uses the enviroCar Server:
- [CITRAM - Citizen Science for Traffic Management](https://www.citram.de/)

## [License](LICENSE) ##

    Copyright (C) 2013-2019 The enviroCar project
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
