FullClient
==========
FullClient is a simple full bitcoinj client that downloads open outputs to a local Postgres database. Uses a bitcoind on localhost. Does not run scripts!

build
-----
mvn clean package

run
---
mvn clean package exec:java -Dexec.mainClass=com.jaeckel.bitcoin.FullClient

create database
---------------
echo  "create database full_mode_db2;" | psql

