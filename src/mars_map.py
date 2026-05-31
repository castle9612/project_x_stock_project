import csv
import heapq
from itertools import permutations
from pathlib import Path

import pandas as pd
from PIL import Image, ImageDraw

try:
    from tqdm import tqdm
except ImportError:
    tqdm = None


BASE_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = BASE_DIR.parent
DATA_DIR = PROJECT_ROOT / "data"
ASSETS_DIR = PROJECT_ROOT / "assets"


def read_dataframes():
    data_dir = DATA_DIR / "problem_2"
    cate_df = pd.read_csv(data_dir / "area_category.csv")
    stru_df = pd.read_csv(data_dir / "area_struct.csv")
    map_df = pd.read_csv(data_dir / "area_map.csv")
    return cate_df, stru_df, map_df


def create_map(data_df):
    map_width = max(data_df["x"]) + 1
    map_height = max(data_df["y"]) + 1
    grid_size = 50

    map_image = Image.new("RGB", (map_width * grid_size, map_height * grid_size), "white")
    draw = ImageDraw.Draw(map_image)

    for x in range(0, map_width * grid_size, grid_size):
        draw.line([(x, 0), (x, map_height * grid_size)], fill="black")
    for y in range(0, map_height * grid_size, grid_size):
        draw.line([(0, y), (map_width * grid_size, y)], fill="black")

    for _, row in data_df.iterrows():
        x = row["x"] * grid_size
        y = row["y"] * grid_size

        if row["mountain"] == 1:
            draw.ellipse(
                [(x + grid_size // 10, y + grid_size // 10), (x + 9 * grid_size // 10, y + 9 * grid_size // 10)],
                fill="brown",
            )
        elif row["category"] in ["U.S. Mars Base Camp", "Korea Mars Base"]:
            base_x = x + grid_size // 2
            base_y = y + grid_size // 2 - grid_size // 4
            draw.polygon(
                [
                    (base_x, base_y + grid_size // 3),
                    (base_x - grid_size // 2, base_y - grid_size // 4),
                    (base_x + grid_size // 2, base_y - grid_size // 4),
                ],
                fill="green",
            )
        elif row["category"] in ["weather sensors", "radder"]:
            draw.rectangle([(x + 10, y + 10), (x + grid_size - 10, y + grid_size - 10)], fill="gray")

    return map_image


def read_path_csv(filename):
    path = []
    with open(filename, "r", encoding="utf-8") as file:
        reader = csv.reader(file)
        next(reader, None)
        for row in reader:
            if len(row) == 2:
                x, y = map(int, row)
                path.append((x, y))
    return path


def read_map_data(df):
    data = {}
    for _, row in df.iterrows():
        x = int(row["x"])
        y = int(row["y"])
        mountain = int(row["mountain"])
        structure = row["category"]
        if mountain != 1:
            data[(x, y)] = structure
    return data


def create_graph(data):
    graph = {}
    for coord in data:
        neighbors = []
        x, y = coord
        for dx, dy in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            neighbor = (x + dx, y + dy)
            if neighbor in data:
                neighbors.append(neighbor)
        graph[coord] = neighbors
    return graph


def dijkstra(graph, start, end):
    distances = {node: float("inf") for node in graph}
    distances[start] = 0
    priority_queue = [(0, start)]
    previous = {node: None for node in graph}

    while priority_queue:
        current_distance, current_node = heapq.heappop(priority_queue)
        if current_node == end:
            break
        for neighbor in graph[current_node]:
            distance = current_distance + 1
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                previous[neighbor] = current_node
                heapq.heappush(priority_queue, (distance, neighbor))

    path = []
    current_node = end
    while current_node is not None:
        path.append(current_node)
        current_node = previous[current_node]
    return list(reversed(path))


def calculate_path_distance(path):
    return max(len(path) - 1, 0)


def calculate_all_around_path(graph, start):
    nodes = list(graph.keys())
    if start not in nodes:
        raise ValueError(f"The start node {start} is not in the graph.")

    nodes.remove(start)
    best_path = None
    shortest_distance = float("inf")
    iterator = permutations(nodes)
    if tqdm is not None:
        iterator = tqdm(iterator)

    for perm in iterator:
        current_path = [start] + list(perm) + [start]
        current_distance = calculate_path_distance(current_path)
        if current_distance < shortest_distance:
            shortest_distance = current_distance
            best_path = current_path

    return best_path


def plot_path(path, map_image, color, line_width, point_size, point_color):
    if not path:
        raise ValueError("The path is empty, unable to plot the path.")

    draw = ImageDraw.Draw(map_image)
    grid_size = 50

    for i in range(len(path) - 1):
        x1, y1 = path[i]
        x2, y2 = path[i + 1]
        draw.line(
            [
                (x1 * grid_size + grid_size // 2, y1 * grid_size + grid_size // 2),
                (x2 * grid_size + grid_size // 2, y2 * grid_size + grid_size // 2),
            ],
            fill=color,
            width=line_width,
        )

    for x, y in path[1:-1]:
        draw.ellipse(
            [
                (x * grid_size + grid_size // 2 - point_size // 2, y * grid_size + grid_size // 2 - point_size // 2),
                (x * grid_size + grid_size // 2 + point_size // 2, y * grid_size + grid_size // 2 + point_size // 2),
            ],
            fill=point_color,
            outline="black",
            width=2,
        )


def save_map(map_image, filename):
    map_image.save(filename)


def main():
    shortest_path_file = DATA_DIR / "problem_3" / "shortest_path.csv"
    shortest_path = read_path_csv(shortest_path_file)
    if not shortest_path:
        raise ValueError("The shortest_path is empty, please check the CSV file.")

    cate_df, str_df, map_df = read_dataframes()
    category_map = dict(zip(cate_df["category"], cate_df["struct"]))
    str_df["category"] = str_df["category"].map(category_map)
    df = str_df.merge(map_df, on=["x", "y"], how="outer")

    map_image = create_map(df)
    plot_path(shortest_path, map_image, color="red", line_width=3, point_size=12, point_color="red")
    ASSETS_DIR.mkdir(exist_ok=True)
    save_map(map_image, ASSETS_DIR / "mars_map_final.png")


if __name__ == "__main__":
    main()
