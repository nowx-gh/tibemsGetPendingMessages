# tibemsGetPendingMessages

## What it does:

 Just print out the number of pending messages from tibco ems queue. You may apply a selector eg, `-before-minutes 120` to only show messages older than now()-120min or apply your own custom selector using the `-selector` option. I'm using it for a simple nagios-check, so I get notified when there are some old messages in our queues.

# compile 

using `javac`, eg:
```
C:\tibco\ems\8.5\samples\java>"C:\Program Files\Java\jdk1.8.0_171\bin\javac.exe" -d . tibjmsGetPendingMessages.java
```

# examples 

```
C:\tibco\ems\8.5\samples\java>java tibjmsGetPendingMessages -server "tcp://tiq-app1.lsag.net:20200" -user admin -password "xx" -queue "sap.converter.idoc.raw.rec.q.v01" -before-minutes 30
Found 11 messages in sap.converter.idoc.raw.rec.q.v01

C:\tibco\ems\8.5\samples\java>java tibjmsGetPendingMessages -server "tcp://tiq-app1.lsag.net:20200" -user admin -password "xx" -queue "sap.converter.idoc.raw.rec.q.v01" -before-hours 2
Found 0 messages in sap.converter.idoc.raw.rec.q.v01

C:\tibco\ems\8.5\samples\java>java tibjmsGetPendingMessages -server "tcp://tiq-app1.lsag.net:20200" -user admin -password "xx" -queue "sap.converter.idoc.raw.rec.q.v01" -selector "JMSTimestamp < 1590952593305"
Found 13 messages in sap.converter.idoc.raw.rec.q.v01
```

# usage

```
C:\tibco\ems\8.5\samples\java>java tibjmsGetPendingMessages -help

Usage: java tibjmsGetPendingMessages [options]

  where options are:

  -server   <server-url>    - EMS server URL, default is local server
  -user     <user-name>     - user name, default is null
  -password <password>      - password, default is null
  -queue    <queue-name>    - queue name, default is null
  -selector <selector-name> - selector string, default is empty aka w/o selector. Can't be used with other filters
  -before-hours <hours>     - only count messages before ? hours. Can't be used with other filters
  -before-minutes <minutes> - only count messages before ? minutes. Can't be used with other filters
```