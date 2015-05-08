# Hydrant

![Hydrant](doc/hydrant.png).

Sucks data from multiple sources and sends to multiple end points

\*\*Inspired by Riemann\*\*

## Status
Alpha .... very Alpha

## Usage

To build ``lein do clean, bin``

To run `` ./target/hydrant [path to config]``

## Configuration

The default and template configuration files can be found in config directory

The configuration is currently broken into 2 parts i.e

- Data Sources : These are sources of streaming data. Currently Twitter (see twitter-source in config) and traversing a web page (see web-source in config)

- Flows : These are components (actually functions) that take a sequence of events/data from the sources and process them.


```clojure
;;Define where the different sources of data
(twitter-source {:con-key "TWITTER-CONSUMER-KEY"
                 :con-secret "TWITTER-CONSUMER-SECRET"
                 :token "TWITTER-CONSUMER-ACCESS-TOKEN"
                 :token-secret "TWITTER-CONSUMER-ACCESS-TOKEN-SECRET"}

                 {:track "SPACE SEPERATED HASHTAGS"}) 

(web-source {:root-url "http://www.google.com"
             :page-crawl-limit 50})
                 
(flows
       sensibly-print-events
       ;;(samsara)
)
```

## Docker

The application can be turned into a docker image by :

- Building the application in the parent directory ``lein do clean, bin``

- Building the image in the parent directory ``docker build -t samsara/hydrant .`` 

The docker image expects certain environment variables to configure the datasources that will be used.

To run the built image to consume twitter :-
- Edit the ./config/env.list file and supply the appropriate values
- run the command ``docker run -d --env-file ./config/env.list -v /tmp/logs/hydrant:/logs  samsara/hydrant``

To view the logs, run ``tail -F /tmp/logs/hydrant/hydrant.log``

The config/fig.yml file is an example of how the image can be run via fig/docker compose.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
