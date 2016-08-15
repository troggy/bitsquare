#!/bin/sh

java -jar gui/target/shaded.jar --useLocalhost=true --nodePort=3332 --appName=Bitsquare-Local-Regtest-Alice --bitcoinNetwork=regtest
