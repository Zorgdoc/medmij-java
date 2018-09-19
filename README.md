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
package com.example;

import nl.zorgdoc.medmij.Whitelist;

public class App
{
    public static void main( String[] args ) throws org.xml.sax.SAXException
    {
        var whitelist = Whitelist.fromString("...");
        System.out.println(whitelist.contains("test"));
    }
}
```

Build and run your app:

```shell
$ mvn package
...
$ java com.example.App
[Fatal Error] :1:1: Content is not allowed in prolog.
...
```
