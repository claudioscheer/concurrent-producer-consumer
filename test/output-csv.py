import os
import sys
import numpy as np

output_folder = os.path.join(os.path.dirname(os.path.abspath(__file__)), "output")


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
    if size == 1000:
        return 0
    elif size == 2000:
        return 1
    elif size == 3000:
        return 2
    return None


def output_2_array():
    data = [[], [], [], [], [], []]
    for f in os.listdir(output_folder):
        if not f.endswith(".txt"):
            continue
        size, threads, throughput = get_data_from_test(os.path.join(output_folder, f))
        data[int(threads / 2 - 1)].insert(get_size_index(size), throughput)
    return data


data = output_2_array()
np.savetxt(sys.argv[1], np.asarray(data), delimiter=",")
