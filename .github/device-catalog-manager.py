import csv
import os


def get_file_line_count():
    if os.name == "posix":
        return int(os.popen("wc -l < excluded_devices.csv").read()[:-1])
    elif os.name == "nt":
        return int(os.popen("type excluded_devices.csv | find /v /c \"\"").read()[:-1]) - 1
    else:
        raise Exception("Who are you?")


output = ""
print(f"Reading CSV file in {os.getcwd()}")
with open("excluded_devices.csv", "r", encoding="utf-8") as file:
    reader = csv.reader(file, delimiter=',')
    row_count = get_file_line_count()
    print(f"Found {row_count} entries")
    if row_count < 1:
        raise Exception("Empty CSV file")
    next(reader)
    name = None
    models = []
    for row in reader:
        print("Entry:", row)
        if name == row[0]:  # multiple technical names
            if not models.__contains__(row[1]):
                models.append(row[1])
        else:  # new model entry
            if name is not None:
                output += "- " + name + " (" + ", ".join(models) + ")\n"
            name = row[0]
            models = [row[1]]
    output += "- " + name + " (" + ", ".join(models) + ")\n"

print("Calculated output:")
print(output)

readme = open("../README.md", "r", encoding="utf-8").read()
marker1 = "<!--- marker1 -->"
marker2 = "<!--- marker2 -->"
list_first_index = readme.find(marker1) + len(marker1) + 1
list_last_index = readme.find(marker2)
print(f"Markers found at {list_first_index} and {list_last_index}")

if list_first_index == -1 or list_last_index == -1:
    raise Exception("Position markers not found")

print("Writing README file")
with open("../README.md", "w", encoding="utf-8") as file:
    file.write(readme[:list_first_index] + output + readme[list_last_index:])
