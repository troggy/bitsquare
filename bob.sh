#!/bin/sh

java -jar gui/target/shaded.jar --useLocalhost=true --nodePort=44442 --appName=Bitsquare-Local-Regtest-Bob --bitcoinNetwork=regtest
