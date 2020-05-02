import os
import sys
import numpy as np


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
    return size, threads, throughput


def get_size_index(size):
    if size == 100:
        return 0
    elif size == 1000:
        return 1
    elif size == 10000:
        return 2
    return None


def output_2_array():
    # One array for each configuration of threads and initial list size.
    data = [
        [[], [], []],
        [[], [], []],
        [[], [], []],
        [[], [], []],
        [[], [], []],
        [[], [], []],
    ]
    for test in os.listdir(output_folder):
        results_test = os.path.join(output_folder, test)
        for f in os.listdir(results_test):
            if not f.endswith(".txt"):
                continue
            size, threads, throughput = get_data_from_test(
                os.path.join(results_test, f)
            )
            # Append at thread position and at the initial list size position.
            data[int(threads / 2 - 1)][get_size_index(size)].append(throughput)
    return data


def get_data_statistics(data):
    new_data = [[], [], [], [], [], []]
    # Loop over threads.
    for i, t in enumerate(data):
        # Loop over initial list sizes.
        for j, s in enumerate(t):
            mean = np.mean(data[i][j])
            std = np.std(data[i][j])
            new_data[i].insert(j, f"{mean}|{std}")
    return new_data


data = output_2_array()
data_statistics = get_data_statistics(data)
np.savetxt(sys.argv[1], np.asarray(data_statistics), delimiter=",", fmt="%s")
