#!/bin/bash

rm -rf ./bin ./results
# I used ant to build the project.
ant

# times_run=1000
# for ((i = 1; i <= times_run; i++))
# do
#    echo $(java -cp bin Test) >> results/JavaLock.txt
# done

java -cp bin Test 1000000 2000000 1 7 1