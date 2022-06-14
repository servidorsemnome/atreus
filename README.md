<h1 align="center">
  <img src="https://i.imgur.com/4LV8MMm.png" />
  <br/>
  Atreus
</h1>

<p align="center">
  The Spigot plugin counterpart of the overly complex SSN.gg authentication system.
</p>

## Building
Make sure you have both [Maven](https://maven.apache.org/download.cgi) and [JDK](https://adoptopenjdk.net) installed (version 8 or up should be fine, but I'm personally using 16), then run:

```sh
mvn install
```

The generated JAR files will be located at the `target` directory.

## Installing
**🚨 This requires both [Gatekeeper](https://github.com/servidorsemnome/gatekeeper) to be running to work properly.**

You can install Atreus just like any other Spigot plugin, drop the JAR file on your `plugins` directory and run the server.

After running it for the first time, close it and fill the `/plugins/Atreus/config.yml` file according to your Gatekeeper and server configuration.
