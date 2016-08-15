#!/bin/sh

java -jar gui/target/shaded.jar --useLocalhost=true --nodePort=2222 --appName=Bitsquare-Local-Regtest-Arbitrator --bitcoinNetwork=regtest
