import os
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

output_folder = os.path.join(os.path.dirname(os.path.abspath(__file__)), "output")
colors = [
    "#5b9bd5",
    "#ed7d31",
    "#a4a4a4",
    "#fdc131",
    "#99731a",
    "#70ad46",
]
patterns = [
    "/",
    "o",
    "x",
]


def get_result_from_line(line):
    return line.split(":")[1].strip()


def output_file_2_dict(file):
    d = {}
    with open(file, "r") as lines:
        for line in lines:
            if line.startswith("Initial list size"):
                d["initial_size"] = int(get_result_from_line(line))
            elif line.startswith("Maximum list size"):
                d["maximum_size"] = int(get_result_from_line(line))
            elif line.startswith("Use warm-up"):
                d["warm_up"] = get_result_from_line(line) == "true"
            elif line.startswith("Number of producers"):
                d["producers"] = int(get_result_from_line(line))
            elif line.startswith("Number of consumers"):
                d["consumers"] = int(get_result_from_line(line))
            elif line.startswith("Current monitor size"):
                d["final_monitor_size"] = int(get_result_from_line(line))
            elif line.startswith("Number of enqueues"):
                d["enqueues"] = int(get_result_from_line(line))
            elif line.startswith("Number of dequeues"):
                d["dequeues"] = int(get_result_from_line(line))
    return d


def output_2_dict():
    data = []
    for f in os.listdir(output_folder):
        if not f.endswith(".txt"):
            continue
        data.append(output_file_2_dict(os.path.join(output_folder, f)))
    return data


def get_enq_deq_plot(
    ax, labels, index, width, enqueues, dequeues, legend_label, current_values
):
    offset = (width * index) + (0.01 * index)
    sum_enq_deq = np.add(enqueues, dequeues)
    avg_enq = np.divide(enqueues, sum_enq_deq)
    avg_deq = np.divide(dequeues, sum_enq_deq)
    rects_enq = ax.bar(
        labels + offset,
        enqueues,
        width,
        label=f"{legend_label} - enq.",
        edgecolor=("white"),
        color=colors[index],
        hatch=patterns[index],
    )
    rects_deq = ax.bar(
        labels + offset,
        dequeues,
        width,
        bottom=enqueues,
        label=f"{legend_label} - deq.",
        edgecolor=("white"),
        color=colors[3 + index],
        hatch=patterns[index],
    )
    auto_label(rects_enq, ax, avg_enq, current_values, True)
    auto_label(rects_deq, ax, avg_deq)


def get_subplot(ax, data, title):
    ax.set_title(title)
    width = 0.3
    labels = np.arange(3)
    data_per_initial_sizes = [
        [x for x in data if x["initial_size"] == 100000],
        [x for x in data if x["initial_size"] == 200000],
        [x for x in data if x["initial_size"] == 300000],
    ]
    maximum_300000_enq = [x["enqueues"] for x in data_per_initial_sizes[0]]
    maximum_300000_deq = [x["dequeues"] for x in data_per_initial_sizes[0]]
    maximum_300000_current = [
        x["final_monitor_size"] for x in data_per_initial_sizes[0]
    ]
    maximum_500000_enq = [x["enqueues"] for x in data_per_initial_sizes[1]]
    maximum_500000_deq = [x["dequeues"] for x in data_per_initial_sizes[1]]
    maximum_500000_current = [
        x["final_monitor_size"] for x in data_per_initial_sizes[1]
    ]
    maximum_1000000_enq = [x["enqueues"] for x in data_per_initial_sizes[2]]
    maximum_1000000_deq = [x["dequeues"] for x in data_per_initial_sizes[2]]
    maximum_1000000_current = [
        x["final_monitor_size"] for x in data_per_initial_sizes[2]
    ]

    get_enq_deq_plot(
        ax,
        labels,
        0,
        width,
        maximum_300000_enq,
        maximum_300000_deq,
        "Max. 300k",
        maximum_300000_current,
    )
    get_enq_deq_plot(
        ax,
        labels,
        1,
        width,
        maximum_500000_enq,
        maximum_500000_deq,
        "Max. 500k",
        maximum_500000_current,
    )
    get_enq_deq_plot(
        ax,
        labels,
        2,
        width,
        maximum_1000000_enq,
        maximum_1000000_deq,
        "Max. 1M",
        maximum_1000000_current,
    )

    ax.set_yticks([0, np.max(np.add(maximum_300000_enq, maximum_300000_deq))])
    ax.set_xticks(labels + width + 0.01)
    ax.set_xticklabels([100000, 200000, 300000])
    ax.legend(loc="upper right")


def auto_label(rects, ax, percentages, current_values=[], bottom=False):
    for i, rect in enumerate(rects):
        rect.set_alpha(0.7)
        height = rect.get_height()
        ax.annotate(
            "{0:.1f}%".format(percentages[i] * 100) + f"\n{height}",
            xy=(0, 0),
            xytext=(
                (rect.get_x() + rect.get_width() / 2),
                height / 2 if bottom else (height + height / 2),
            ),
            textcoords="data",
            ha="center",
            va="center",
        )
        if bottom:
            ax.annotate(
                "{}".format(current_values[i]),
                xy=(0, 0),
                xytext=((rect.get_x() + rect.get_width() / 2), height + height,),
                textcoords="data",
                ha="center",
                va="bottom",
            )


def get_plot(data, used_warm_up, output_file):
    data_sorted = sorted(
        data,
        key=lambda x: (
            x["initial_size"],
            x["maximum_size"],
            x["producers"],
            x["consumers"],
        ),
    )
    plots_data = [
        [x for x in data_sorted if x["producers"] == 1 and x["consumers"] == 11],
        [x for x in data_sorted if x["producers"] == 4 and x["consumers"] == 8],
        [x for x in data_sorted if x["producers"] == 8 and x["consumers"] == 4],
        [x for x in data_sorted if x["producers"] == 11 and x["consumers"] == 1],
    ]
    fig, ax = plt.subplots(nrows=2, ncols=2, figsize=(28, 24))
    # fig.tight_layout()
    warm_up_tile = ", with warm-up" if used_warm_up else ", without warm-up"
    get_subplot(ax[0, 0], plots_data[0], f"1 producer, 11 consumers{warm_up_tile}")
    get_subplot(ax[0, 1], plots_data[1], f"4 producers, 8 consumers{warm_up_tile}")
    get_subplot(ax[1, 0], plots_data[2], f"8 producers, 4 consumers{warm_up_tile}")
    get_subplot(ax[1, 1], plots_data[3], f"11 producers, 1 consumer{warm_up_tile}")

    plt.savefig(output_file, transparent=True)


data = output_2_dict()
data_with_warm_up = [x for x in data if x["warm_up"]]
data_without_warm_up = [x for x in data if not x["warm_up"]]

get_plot(data_with_warm_up, True, "warm-up.pdf")
get_plot(data_without_warm_up, False, "no-warm-up.pdf")
