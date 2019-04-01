# Mule CoAP Server Connector
![Mule-Coap logo](icons/coap-logo.svg)

Mule CoAP connector is an Anypoint Connector implementation of the [RFC7252 - Constrained Application Protocol](http://tools.ietf.org/html/rfc7252). 
With it Mule applications become CoAP capable and can communicate with other CoAP capable devices and services, realising Internet of Things solutions (IoT). 

The connector uses Californium, a Java CoAP implementation. More information about Californium and CoAP can be found at:

* [http://www.eclipse.org/californium/](http://www.eclipse.org/californium/)
* [http://coap.technology/](http://coap.technology/).

This component - the Mule CoAP Server Connector - is one of three parts of the Mule CoAP package.  
The other two being the Mule CoAP Client Connector and the Mule CoAP Commons component . 

The CoapServer Connector adds CoAP server capability to [Mule enterprise service bus](https://www.mulesoft.com/).
With it Mule applications can implement services that can be accessed by clients using the CoAP protocol. 

The complete Mule CoAP Connector documentation can be found on [Teslanet.nl](http://www.teslanet.nl)

## Mule supported versions
* Mule 3.8
* Mule 3.9

## CoAP supported versions
* [IETF rfc 6690](https://tools.ietf.org/html/rfc6690)
* [IETF rfc 7252](https://tools.ietf.org/html/rfc7252)
* [IETF rfc 7641](https://tools.ietf.org/html/rfc7641)
* [IETF rfc 7959](https://tools.ietf.org/html/rfc7959)

## Dependencies
* [Californium](https://www.eclipse.org/californium/) 1.0.7
* [Mule Coap Commons](https://github.com/teslanet-nl/mule-coap-commons) 1.1.0 

## Installation

Easiest way to install releases into Anypoint Studio 6 is by installing the Eclipse Plugin using the update site.. 
The Mule CoAP Connector update site url is:

```
http://www.teslanet.nl/mule-coap-connector/update/
```

Installation of release or development versions from source, is done by dowloading sources or cloning git-repository from Github:

* Import source into [Anypoint Studio 6](https://www.mulesoft.com/platform/studio)
* Select the imported project
* Build and install: Context Menu -> Anypoint Connector / Install or Update


## Usage
See the Userguide on [Teslanet.nl](http://www.teslanet.nl/mule-coap-client-connector_1_0/doc/userguide/index.xhtml)

### Using in Maven Projects

Mule CoAP artefact releases will be published to [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cmule-coap-server).
To use Mule CoAP Server Connector in maven projects add following dependency
to your `pom.xml`.

```xml
  
    <dependency>
            <groupId>nl.teslanet.mule.transport.coap</groupId>
            <artifactId>mule-coap-server-connector</artifactId>
            <version>1.0.1</version>
    </dependency>
  
```

# Reporting Issues

You can report new issues at [github](https://github.com/teslanet-nl/mule-coap-server-connector/issues).

# Contact

Questions or remarks? Create an issue on [github](https://github.com/teslanet-nl/mule-coap-server-connector/issues)

# Contributing

Use issues or pull-requests on your fork.
