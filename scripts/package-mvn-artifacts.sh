#!/bin/bash

set -eu
set -o pipefail

cd "$(pwd)"

git clean -xdf .
mvn clean deploy
cp -r target/finbench-mvn/* ../finbench-mvn/
