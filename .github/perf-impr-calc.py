import csv
import os

print(f"Reading CSV file in {os.getcwd()}")
with open("performance-data.csv", "r") as file:
    reader = csv.reader(file, delimiter=',')
    next(reader)
    base_value = next(reader)[1]
    new_value = next(reader)[1]
    percent = ((base_value - new_value) / base_value) * 100

print("Average performance enhancement:", percent)
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

