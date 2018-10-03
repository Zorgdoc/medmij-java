# medmij-java

## Installation

Clone the repo, build and install using maven:

```shell
$ PATH_TO_CLONE=~/example/medmij-java
$ git clone https://github.com/Zorgdoc/medmij-java.git $PATH_TO_CLONE
...
$ mvn package
...
$ mvn install
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
package com.example;

import java.io.IOException;
import java.net.URL;

import nl.zorgdoc.medmij.Whitelist;
import nl.zorgdoc.medmij.ZAL;
import nl.zorgdoc.medmij.PGOOAuth;
import nl.zorgdoc.medmij.ZAOAuth;

public class App {

    public static void main(String[] args) throws org.xml.sax.SAXException, IOException {
        final URL WHITELIST_URL = new URL(
                "http://gids.samenbeter.org/openpgoexamples/1.0/whitelist-voorbeeld-v1.0.xml");
        final URL ZAL_URL = new URL(
                "http://gids.samenbeter.org/openpgoexamples/1.0/zorgaanbiederslijst-voorbeeld-v1.0.xml");

        final String HOSTNAME = "medmij.deenigeechtepgo.nl";

        var whitelist = Whitelist.fromUrl(WHITELIST_URL);
        System.out.printf("Is %s on whitelist: %s\n\n", HOSTNAME, whitelist.contains(HOSTNAME));

        var zal = ZAL.fromUrl(ZAL_URL);
        var za = zal.getByName("umcharderwijk@medmij");
        var geg = za.getGegevensdienstById("4");
        System.out.printf("AuthorizationEndpointUri: %s\n\n", geg.authorizationEndpointuri);

        System.out.printf("Link to ZA:\n%s\n\n",
                PGOOAuth.makeAuthURL(geg, HOSTNAME, "https://pgo.example.com/oauth", "abcd"));

        System.out.printf("Redirect to PGO:\n%s\n\n",
                ZAOAuth.makeRedirectURL("https://pgo.example.com/oauth", "xyz", "abcd"));

        System.out.printf("Get token:\n");
        String token;
        try {
            token = PGOOAuth.getAccessToken(geg, "xyz", "https://pgo.example.com/oauth");
        } catch (IOException e) {
            System.out.printf("Exception: %s\n", e);
            return;
        }
        System.out.printf("Token = %s\n", token);
    }
}

```

Build and run your app:

```shell
$ mvn package
...
$ java com.example.App
Is medmij.deenigeechtepgo.nl on whitelist: true

AuthorizationEndpointUri: https://medmij.za982.xisbridge.net/oauth/authorize

Link to ZA:
https://medmij.za982.xisbridge.net/oauth/authorize?response_type=code&client_id=medmij.deenigeechtepgo.nl&redirect_uri=https%3A%2F%2Fpgo.example.com%2Foauth&scope=umcharderwijk%7E4&state=abcd

Redirect to PGO:
https://pgo.example.com/oauth/cb?code=xyz&state=abcd

Get token:
Exception: java.net.UnknownHostException: medmij.xisbridge.net
```
