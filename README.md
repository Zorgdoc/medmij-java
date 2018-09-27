# medmij-java

## Installation

Clone the repo, build and install using maven:

```shell
$ PATH_TO_CLONE=~/example/medmij-java
$ git clone https://github.com/Zorgdoc/medmij-java.git $PATH_TO_CLONE
...
$ mvn package
...
$ mvn install:install-file -Dfile=target/medmij-1.0-SNAPSHOT.jar -DpomFile=pom.xml
...
```

## Usage

Add the dependency to your `pom.xml`:

```xml
    <dependency>
      <groupId>nl.zorgdoc.medmij</groupId>
      <artifactId>medmij</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

Add an `import` to `App.java` and use the API:

```java
package com.mycompany.app;

import java.io.IOException;
import java.net.URL;

import nl.zorgdoc.medmij.Whitelist;
import nl.zorgdoc.medmij.ZAL;
import nl.zorgdoc.medmij.OAuthclientList;

public class App {

    public static void main(String[] args) throws org.xml.sax.SAXException, IOException {
        final URL WHITELIST_URL = new URL(
                "http://gids.samenbeter.org/openpgoexamples/1.0/whitelist-voorbeeld-v1.0.xml");
        final URL ZAL_URL = new URL(
                "http://gids.samenbeter.org/openpgoexamples/1.0/zorgaanbiederslijst-voorbeeld-v1.0.xml");
        final URL OCL_URL = new URL(
                "http://gids.samenbeter.org/openpgoexamples/1.0/oauthclientlist-voorbeeld-v1.0.xml");

        var whitelist = Whitelist.fromUrl(WHITELIST_URL);
        System.out.println(whitelist.contains("test"));

        var zal = ZAL.fromUrl(ZAL_URL);
        var za = zal.getByName("umcharderwijk@medmij");
        System.out.println(za.getGegevensdienstById("4").authorizationEndpointuri);

        var ocl = OAuthclientList.fromUrl(OCL_URL);
        var oc = ocl.getByHostname("medmij.deenigeechtepgo.nl");
        System.out.println(oc.organisatienaam);
    }
}
```

Build and run your app:

```shell
$ mvn package
...
$ java com.example.App
false
https://medmij.za982.xisbridge.net/oauth/authorize
De Enige Echte PGO
```
