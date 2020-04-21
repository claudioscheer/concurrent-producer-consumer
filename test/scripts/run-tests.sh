#!/bin/bash

rm -rf ../bin
# ./output
cd ..
ant
cd -
# mkdir output

# initial_sizes=(100000 200000 300000)
initial_sizes=(300000)
# capacities=(300000 500000 1000000)
capacities=(1000000)
threads=(6 8 10 12)
for ((i = 0; i < 3; i++)); do
    for t in "${threads[@]}"; do
        initial_size=${initial_sizes[i]}
        capacity=${capacities[i]}
        java -cp ../bin main/Test $initial_size $capacity $t > output/$initial_size-$capacity-$t.txt
    done
done