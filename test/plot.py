import sys
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


data = pd.read_hdf(sys.argv[1], key="lists")

df_plot = pd.DataFrame()
df_plot["100"] = data["100", "throughput"].mean(axis=1)
df_plot["100_std"] = data["100", "throughput"].std(axis=1)
df_plot["1000"] = data["1000", "throughput"].mean(axis=1)
df_plot["1000_std"] = data["1000", "throughput"].std(axis=1)
df_plot["10000"] = data["10000", "throughput"].mean(axis=1)
df_plot["10000_std"] = data["10000", "throughput"].std(axis=1)

df_plot["list_size_100"] = data["100", "avg_list_size"].mean(axis=1)
df_plot["list_size_1000"] = data["1000", "avg_list_size"].mean(axis=1)
df_plot["list_size_10000"] = data["10000", "avg_list_size"].mean(axis=1)

title = f"{sys.argv[2]} Throughput"
ylabel = "Throughput (operations/second)"
columns = ["100", "1000", "10000"]
label_columns = ["100", "100_std", "1000", "1000_std", "10000", "10000_std"]
show_std = True
if len(sys.argv) > 3 and sys.argv[3] == "list_size":
    show_std = False
    title = f"{sys.argv[2]} Average List Size"
    ylabel = "Average List Size"
    columns = ["list_size_100", "list_size_1000", "list_size_10000"]
    label_columns = columns

ax = df_plot[columns].plot(marker="X", figsize=(12, 8))
ax.set(xlabel="Threads", ylabel=ylabel)
ax.legend(["100", "1k", "10k"])
ax.grid()

df_plot_values = df_plot[label_columns].values

for i, y_column in enumerate(df_plot_values):
    for j, c in enumerate(y_column):
        if not show_std or j % 2 == 0:
            text = (
                "{:.0f}-{:.0f}".format(c, y_column[j + 1])
                if show_std
                else "{:.2f}".format(c)
            )
            ax.annotate(text, xy=(i, c), textcoords="data")

plt.savefig(f"plots/{title.replace(' ', '')}.pdf")
