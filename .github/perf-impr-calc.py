import csv
import os


def get_file_line_count():
    if os.name == "posix":
        return int(os.popen("wc -l < performance-data.csv").read()[:-1])
    elif os.name == "nt":
        return int(os.popen("type performance-data.csv | find /v /c \"\"").read()[:-1]) - 1
    else:
        raise Exception("Who are you?")


print(f"Reading CSV file in {os.getcwd()}")
with open("performance-data.csv", "r") as file:
    reader = csv.reader(file, delimiter=',')
    row_count = get_file_line_count()
    print(f"Found {row_count} entries")
    if row_count < 1:
        raise Exception("Empty CSV file")
    next(reader)
    coefficient = 0
    for row in reader:
        print("Entry:", row)
        naked = int(row[1])
        baseline = int(row[2])
        temp_coefficient = (naked - baseline) / naked
        print("Coefficient: ", temp_coefficient)
        coefficient += temp_coefficient

average_percentage = round(coefficient * 100 / row_count, 2)
print("Average performance enhancement:", average_percentage)
# changelog = os.getenv("CHANGELOG")
# # changelog = open("changelog.txt", "r", encoding="utf-8").read()
# marker = "<<marker1>>"
# first_index = changelog.find(marker)
# if first_index == -1:
#     raise Exception("Position marker not found")
#
# last_index = first_index + len(marker)
# print(f"Marker index at {first_index}")
#
# print("Writing changelog output")
# changelog = changelog[:first_index] + str(average_percentage) + "%" + changelog[last_index:]
# env_file = os.getenv('GITHUB_ENV')
# with open(env_file, "a") as file:
#     file.write(f"NEW_CHANGELOG={changelog}")

