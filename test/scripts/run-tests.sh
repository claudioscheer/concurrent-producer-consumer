#!/bin/bash

# example:
# ./scripts/run-tests.sh coarse-list "Coarse List"
# ./scripts/run-tests.sh fine-list "Fine List"
# ./scripts/run-tests.sh optimistic-list "Optimistic List"

rm -rf ../bin
cd ..
ant
cd -
mkdir -p raw-data

output_name=$1
rm -rf raw-data/$output_name
mkdir raw-data/$output_name

initial_sizes=(100 1000 10000)
threads=(2 4 6 8 10 12)
tests_count=3
for ((i = 1; i <= $tests_count; i++)); do
    mkdir raw-data/$output_name/$i
    for initial_size in "${initial_sizes[@]}"; do
        for t in "${threads[@]}"; do
            java -cp ../bin main/Test $initial_size $t > raw-data/$output_name/$i/$initial_size-$t.txt
        done
    done
done

python output-hdf.py hdf/$output_name.h5 raw-data/$output_name
python plot.py hdf/$output_name.h5 "$2"
python plot.py hdf/$output_name.h5 "$2" list_size