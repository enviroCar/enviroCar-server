---
layout: default
---

# Developer Guide #


## Build & Installation ##

* Install and run [MongoDB][mongo] >2.4.
* Clone the [repository][repo] and switch to the directory.
* Configure the connection in `mongo/src/main/resources/mongo.properties` (if needed).
* Run `mvn clean install`.
* Deploy the `war` file in `webapp/target` to a application server of your choice (e.g. [Apache Tomcat][tomcat]).

## Guice ##

The service makes heavy use of [Guice][guice]. Implementations are bound in a `Module`. For a detailed documentation see the [Guice Wiki][guice-doc]. The service loads available module using the Java [ServiceLoader API][serviceloader].

```java
public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataService.class).to(DataServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(FriendService.class).to(FriendServiceImpl.class);
        bind(GroupService.class).to(GroupServiceImpl.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(ActivityListener.class).asEagerSingleton();
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }
}
```


## Event Bus ##

The service offers an extension API using a [Guava EventBus][eventbus]. The following events are accessible for extensions:

* `ChangedGroupEvent`: fired after the description of a group was changed
* `ChangedMeasurementEvent`: fired after a measurements was changed
* `ChangedProfileEvent`: fired after the profile of a user was changed
* `ChangedTrackEvent`: fired after a track was changed
* `CreatedGroupEvent`: fired after a user created a new group
* `CreatedMeasurementEvent`: fired after a user created a new measurement
* `CreatedPhenomenonEvent`: fired after a user created a new phenomenon
* `CreatedSensorEvent`: fired after a user created a new sensor
* `CreatedTrackEvent`: fired after a user created a new track
* `CreatedUserEvent`: fired after a new user was created
* `DeletedGroupEvent`: fired after a user deleted a group
* `DeletedMeasurementEvent`: fired after a user deleted a measurement
* `DeletedTrackEvent`: fired after a user deleted a track
* `DeletedUserEvent`: fired after a user was deleted
* `FriendedUserEvent`: fired after a user friended another user
* `JoinedGroupEvent`: fired after a user joined a group
* `LeftGroupEvent`: fired after a user left a group
* `UnfriendedUserEvent`: fired after a user unfriended another user

To subscribe to an event type you have to annotate some method with `@Subscribe`. The parameter type determines the type of event:

```java
public class SomeListener {
    private final DataService service;

    @Inject
    public SomeListener(DataService service) {
        this.service = service;
    }

    @Subscribe
    public void onCreatedTrackEvent(CreatedTrackEvent e) {
        doSomething(e.getUser(), service.getMeasurements(
            new MeasurementFilter(e.getTrack())));
    }
}
```
A class that wants to receive events has to be created by Guice. For this you could declare it as a eager singleton in any module:

```java
bind(SomeListener.class).asEagerSingleton();
```

## RDF API ##

Additional resources can be linked by implementing the interface `RDFLinker<T>` for the entity type `T`:

```java
import javax.ws.rs.core.UriBuilder;
import org.envirocar.server.rest.rights.AccessRights;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public interface RDFLinker<T> {
    void link(Model m, T t, AccessRights rights,
              Resource r, Provider<UriBuilder> uriBuilder);
}
```

The additional triples have to be added to the `Model m`. URIs to other entities can be build using the supplied `UriBuilder`. The rights of the current user to see properties and entities can be obtained using the `AccessRights` object. The following example adds latitude and longitude to a point measurement using the [W3C Basic Geo vocabulary][w3cgeo]:


```java
import javax.ws.rs.core.UriBuilder;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.encoding.rdf.vocab.W3CGeo;
import org.envirocar.server.rest.rights.AccessRights;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.vividsolutions.jts.geom.Point;

public class W3CGeoMeasurementLinker implements RDFLinker<Measurement> {
    @Override
    public void link(Model m, Measurement t, AccessRights rights,
                     Resource r, Provider<UriBuilder> uriBuilder) {
        if (t.getGeometry() instanceof Point) {
            m.setNsPrefix(W3CGeo.PREFIX, W3CGeo.URI);
            Point p = (Point) t.getGeometry();
            r.addLiteral(W3CGeo.lat, p.getY())
             .addLiteral(W3CGeo.lon, p.getX());
        }
    }
}
```

To let the service automatically faciliate the linker it has to be bound in any Guice module:

```java
Multibinder<RDFLinker<Measurement>> b = Multibinder.newSetBinder(
        binder(), new TypeLiteral<RDFLinker<Measurement>>() {});
b.addBinding().to(W3CGeoMeasurementLinker.class);
```

[eventbus]: https://code.google.com/p/guava-libraries/wiki/EventBusExplained "EventBus"
[serviceloader]: http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html "ServiceLoader API"
[guice]: https://code.google.com/p/google-guice/ "Guice"
[guice-doc]: https://code.google.com/p/google-guice/wiki/GettingStarted "Guice Documentation"
[tomcat]: http://tomcat.apache.org/ "Apache Tomcat"
[mongo]: http://www.mongodb.org/ "MongoDB"
[w3cgeo]: http://www.w3.org/2003/01/geo/ "W3C Basic Geo"
[repo]: https://github.com/enviroCar/enviroCar-server.git "GitHub Repository"
