#!/bin/bash

rm -rf ../bin ./output
cd ..
ant
cd -
mkdir output

initial_sizes=(100000 200000 300000)
maximum_sizes=(300000 500000 1000000)
threads=(6 12)
for ((i = 0; i < 3; i++)); do
    for t in "${threads[@]}"; do
        initial_size=${initial_sizes[i]}
        maximum_size=${maximum_sizes[i]}
        java -cp ../bin main/Test $initial_size $maximum_size $t > output/$initial_size-$maximum_size-$t.txt
    done
done