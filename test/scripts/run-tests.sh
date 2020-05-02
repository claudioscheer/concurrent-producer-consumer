#!/bin/bash

# example:
# ./scripts/run-tests.sh coarse-list "Coarse List Throughput"

rm -rf ../bin
cd ..
ant
cd -
mkdir -p raw-data

output_name=$1
rm -rf raw-data/$output_name
mkdir raw-data/$output_name

# each test takes (20s + 90s) ~= 2m
# 2m * 18 * 10 = 360m = 6h

initial_sizes=(100 1000 10000)
threads=(2 4 6 8 10 12)
tests_count=10
for ((i = 1; i <= $tests_count; i++)); do
    mkdir raw-data/$output_name/$i
    for initial_size in "${initial_sizes[@]}"; do
        for t in "${threads[@]}"; do
            java -cp ../bin main/Test $initial_size $t > raw-data/$output_name/$i/$initial_size-$t.txt
        done
    done
done

python output-csv.py csv/$output_name.csv raw-data/$output_name
python plot.py csv/$output_name.csv "$2"