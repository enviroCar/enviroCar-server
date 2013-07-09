---
layout: default
---

# Linked Open Data #

Every resource is available as RDF. To request the content as Linked Data you have to set the appropiate `Accept` header.

For [Turtle][turtle]:

```
curl -u jakob1:*****  -H "Accept: text/turtle" https://giv-car.uni-muenster.de/dev/rest/users/jakob1
```
```
@base          <https://giv-car.uni-muenster.de/dev/rest/> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
@prefix foaf:  <http://xmlns.com/foaf/0.1/> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .
@prefix dcterms: <http://purl.org/dc/elements/1.1/> .

<users/jakob2>  a  foaf:Person .
<users/Turbomartin>  a  foaf:Person .
<users/ChristianK>  a  foaf:Person .
<users/OraibMiq>  a  foaf:Person .
<users/AlbertRemke>  a  foaf:Person .
<users/dnnsdnns>  a  foaf:Person .
<groups/Product%20Owner>
        a            foaf:Group ;
        foaf:member  <users/jakob1> .
<users/jakob1>  a       foaf:Person ;
        dcterms:rights  "http://opendatacommons.org/licenses/odbl/" ;
        vcard:EMAIL     "jakob@jakob.de" ;
        vcard:NICKNAME  "jakob1" ;
        foaf:img        <users/jakob1/avatar> ;
        foaf:knows      <users/AlbertRemke> ,
                        <users/ChristianK> ,
                        <users/dnnsdnns> ,
                        <users/OraibMiq> ,
                        <users/Turbomartin> ,
                        <users/jakob2> ;
        foaf:mbox       "mailto:jakob@jakob.de"^^xsd:string ;
        foaf:nick       "jakob1"^^xsd:string .
```

For [RDF/XML][rdf]:

```
curl -u jakob1:*****  -H "Accept: application/rdf+xml" https://giv-car.uni-muenster.de/dev/rest/users/jakob1
```
```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:vcard="http://www.w3.org/2001/vcard-rdf/3.0#"
    xmlns:dcterms="http://purl.org/dc/elements/1.1/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
  <foaf:Person rdf:about="">
    <foaf:knows>
      <foaf:Person rdf:about="AlbertRemke"/>
    </foaf:knows>
    <vcard:EMAIL>jakob@jakob.de</vcard:EMAIL>
    <dcterms:rights>http://opendatacommons.org/licenses/odbl/</dcterms:rights>
    <foaf:knows>
      <foaf:Person rdf:about="ChristianK"/>
    </foaf:knows>
    <foaf:knows>
      <foaf:Person rdf:about="dnnsdnns"/>
    </foaf:knows>
    <foaf:knows>
      <foaf:Person rdf:about="OraibMiq"/>
    </foaf:knows>
    <foaf:nick rdf:datatype="http://www.w3.org/2001/XMLSchema#string">jakob1</foaf:nick>
    <foaf:mbox rdf:datatype="http://www.w3.org/2001/XMLSchema#string">mailto:jakob@jakob.de</foaf:mbox>
    <foaf:knows>
      <foaf:Person rdf:about="Turbomartin"/>
    </foaf:knows>
    <foaf:img rdf:resource="jakob1/avatar"/>
    <foaf:knows>
      <foaf:Person rdf:about="jakob2"/>
    </foaf:knows>
    <vcard:NICKNAME>jakob1</vcard:NICKNAME>
  </foaf:Person>
  <foaf:Group rdf:about="../groups/Product%20Owner">
    <foaf:member rdf:resource=""/>
  </foaf:Group>
</rdf:RDF>
```

[turtle]: http://www.w3.org/TeamSubmission/turtle/ "Turtle"
[rdf]: http://www.w3.org/RDF/ "RDF"
