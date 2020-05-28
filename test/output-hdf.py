import os
import sys
import numpy as np
import pandas as pd


output_folder = os.path.join(os.path.dirname(os.path.abspath(__file__)), sys.argv[2])


def get_result_from_line(line):
    return line.split(":")[1].strip()


def get_data_from_test(file):
    with open(file, "r") as lines:
        for line in lines:
            if line.startswith("Initial list size"):
                size = int(get_result_from_line(line))
            elif line.startswith("Number of threads"):
                threads = int(get_result_from_line(line))
            elif line.startswith("Operations throughput per second"):
                throughput = float(get_result_from_line(line))
            elif line.startswith("Average list size"):
                avg_list_size = float(get_result_from_line(line))
            elif line.startswith("Total of add operations"):
                add_operations = float(get_result_from_line(line))
            elif line.startswith("Total of remove operations"):
                remove_operations = float(get_result_from_line(line))
            elif line.startswith("Total of contains operations"):
                contains_operations = float(get_result_from_line(line))
            elif line.startswith("Total of listSize operations"):
                list_size_operations = float(get_result_from_line(line))
    return (
        size,
        threads,
        throughput,
        avg_list_size,
        add_operations,
        remove_operations,
        contains_operations,
        list_size_operations,
    )


def output_2_array():
    # One array for each configuration of threads and initial list size.
    data = pd.DataFrame(
        [],
        columns=pd.MultiIndex.from_product(
            [
                ["100", "1000", "10000"],
                [
                    "throughput",
                    "avg_list_size",
                    "add_operations",
                    "remove_operations",
                    "contains_operations",
                    "list_size_operations",
                ],
                [str(x + 1) for x in range(10)],
            ]
        ),
        index=["2", "4", "6", "8", "10", "12"],
        dtype=float,
    )
    for test in os.listdir(output_folder):
        results_test = os.path.join(output_folder, test)
        for f in os.listdir(results_test):
            if not f.endswith(".txt"):
                continue
            (
                size,
                threads,
                throughput,
                avg_list_size,
                add_operations,
                remove_operations,
                contains_operations,
                list_size_operations,
            ) = get_data_from_test(os.path.join(results_test, f))
            data.at[str(threads), (str(size), "throughput", test)] = throughput
            data.at[str(threads), (str(size), "avg_list_size", test)] = avg_list_size
            data.at[str(threads), (str(size), "add_operations", test)] = add_operations
            data.at[
                str(threads), (str(size), "remove_operations", test)
            ] = remove_operations
            data.at[
                str(threads), (str(size), "contains_operations", test)
            ] = contains_operations
            data.at[
                str(threads), (str(size), "list_size_operations", test)
            ] = list_size_operations
    return data


data = output_2_array()
data.to_hdf(sys.argv[1], key="lists")
