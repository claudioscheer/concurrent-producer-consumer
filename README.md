## About the project

I used [Ant](https://ant.apache.org/) to build the project. Below is the description of each relevant file and folder in the project.

- `build.xml`: settings used by Ant to build the project;
- `src`: source code;
  - `src/main/Test.java`: the class that performs the test;
  - `src/lists`: the implementation of the lists;
- `test`: results and scripts used to test the performance of lists;
  - `test/raw-data`: the raw output of the class `Test.java`, for each tested list;
    - the naming convention for each file is as follows: `INITIAL_LIST_SIZE-THREADS_NUMBER.txt`;
  - `test/hdf`: Hierarchical Data Format (HDF) files created from raw data, using the `test/output-hdf.py` script;
  - `test/plot.py`: creates a plot, using [Matplotlib](https://matplotlib.org/), based on previously generated HDF files;
  - `test/plots`: plots showing the throughput per second for each list;

Information on the throughput of each operation (add, remove, contains, listSize) and the average size of the list is shown in the raw data.


## Report

You can see the final report [here](./report/report.pdf).