#!/bin/bash

set -eu
set -o pipefail

cd "$(pwd)"

git clean -xdf .
mvn clean deploy
cp -r target/ldbc_finbench_mvn/* ../ldbc_finbench_mvn/
