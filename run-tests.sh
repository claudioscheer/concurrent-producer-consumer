#!/bin/bash

rm -rf ./bin ./output
# I used ant to build the project.
ant

mkdir output

warm_up=(0 1)
initial_sizes=(100000 200000 300000)
maximum_sizes=(300000 500000 1000000)
producers=(1 4 8 11) # the sum of producers and consumers must be 12
for w in "${warm_up[@]}"; do
    for i in "${initial_sizes[@]}"; do
        for m in "${maximum_sizes[@]}"; do
            for p in "${producers[@]}"; do
                c=$((12 - p))
                java -cp bin Test $i $m $w $p $c > output/$i-$m-$w-$p-$c.txt
            done
        done
    done
done