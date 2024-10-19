import csv
import os

print(f"Reading CSV file in {os.getcwd()}")
with open("performance-data.csv", "r") as file:
    reader = csv.reader(file, delimiter=',')
    next(reader)
    base_value = int(next(reader)[1])
    new_value = int(next(reader)[1])
    percent = ((base_value - new_value) / base_value) * 100

rounded_percent = round(percent, 2)
print("Average performance enhancement:", rounded_percent)
changelog = os.getenv("CHANGELOG")
# changelog = open("changelog.txt", "r", encoding="utf-8").read()
marker = "<<marker1>>"
first_index = changelog.find(marker)
if first_index == -1:
    raise Exception("Position marker not found")

print(f"Marker index at {first_index}")

print("Writing changelog output")
changelog = changelog.replace(marker, str(rounded_percent))
print(f"::set-output name=changelog::{changelog}")
