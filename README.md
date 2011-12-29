# The Minecraft JMX Plugin
 -------
This is a plugin for the Bukkit project

This plug-in Allows you to Monitor your Server via JMX. Once enabled it will listen to a port that you configure for connections
from a JMX client ( Jconsole , MX4J , etc. ). Aside form providing Statistics on the JVM and the Minecraft Process it hooks into
various minecraft actions and provides player and server stats.

## Configuration
This plugin now uses the global bukkit configuration file. It currently supports these options

ip
username
password
port

 __ You Must Change the Username and Password __

## Use
You can connect to a properly configured server with JConsole
e.g. run jconsole , enter service:jmx:rmi:///jndi/rmi://<Your IP>:9999/jmxrmi , username defaults to admin password defaults to passwd

