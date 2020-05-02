import sys
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


csv = np.loadtxt(sys.argv[1], delimiter=",", dtype=str)
x = np.arange(2, 14.0, 2)
data = [[], [], [], [], [], []]
std = [[], [], [], [], [], []]
# Threads.
for i, t in enumerate(csv):
    # Initial list sizes.
    for c, s in enumerate(t):
        data[i].insert(c, float(csv[i][c].split("|")[0]))
        std[i].insert(c, float(csv[i][c].split("|")[1]))

fig, ax = plt.subplots(figsize=(12, 8))
ax.plot(x, data, marker="X")
ax.legend(["100", "1k", "10k"])

for i, y_column in enumerate(data):
    for j, c in enumerate(y_column):
        ax.annotate(
            "{:.2f}-{:.2f}".format(c, std[i][j]),
            xy=((i * 2 + 2), c),
            textcoords="data",
        )

ax.set(
    xlabel="Threads", ylabel="Throughput (operations/second)", title=sys.argv[2],
)
ax.grid()

fig.savefig(f"plots/{sys.argv[2].replace(' ', '')}.pdf")
