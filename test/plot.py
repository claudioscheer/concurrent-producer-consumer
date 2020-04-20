import sys
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


x = np.arange(2, 14.0, 2)
data = np.loadtxt(sys.argv[1], delimiter=",", dtype=float)

fig, ax = plt.subplots(figsize=(12, 8))
ax.plot(x, data, marker="X")
ax.legend(["100k", "200k", "300k"])

for i, y_column in enumerate(data):
    for j in y_column:
        ax.annotate(
            "{:10.2f}".format(j), xy=((i * 2 + 2), (j + j * 0.01)), textcoords="data"
        )

ax.set(
    xlabel="Threads", ylabel="Throughput (operations/second)", title=sys.argv[2],
)
ax.grid()

fig.savefig(f"plots/{sys.argv[2].replace(' ', '')}.pdf")
