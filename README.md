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

The configuration is currently broken into 2 parts i.e

- Data Sources
- Flows

TODO explain the above 

```clojure
;;Define where the different sources of data
(twitter-source {:con-key "TWITTER-CONSUMER-KEY"
                 :con-secret "TWITTER-CONSUMER-SECRET"
                 :token "TWITTER-CONSUMER-ACCESS-TOKEN"
                 :token-secret "TWITTER-CONSUMER-ACCESS-TOKEN-SECRET"}

                 {:track "SPACE SEPERATED HASHTAGS"}) 

                 
(flows
       #(log/info %) 
)
```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
