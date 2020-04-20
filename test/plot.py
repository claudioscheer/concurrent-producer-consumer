import sys
import matplotlib
import matplotlib.pyplot as plt
import numpy as np


x = np.arange(2, 14.0, 2)
y = np.loadtxt(sys.argv[1], delimiter=",", dtype=float)

fig, ax = plt.subplots(figsize=(12, 8))
ax.plot(x, y, marker="X")
ax.legend(["1k", "2k", "3k"])

ax.set(
    xlabel="Threads", ylabel="Throughput (operations/second)", title=sys.argv[2],
)
ax.grid()

fig.savefig(f"{sys.argv[2].replace(' ', '')}.pdf")
