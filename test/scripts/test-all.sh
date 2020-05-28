#!/bin/bash

./scripts/run-tests.sh coarse-list "Coarse List"
./scripts/run-tests.sh fine-list "Fine List"
./scripts/run-tests.sh optimistic-list "Optimistic List"
./scripts/run-tests.sh lazy-list "Lazy List"
./scripts/run-tests.sh lock-free-list "Lock Free List"