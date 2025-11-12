import csv
import os
from collections import OrderedDict
from functools import cmp_to_key


def custom_compare(a, b):
    a = a.lower()
    b = b.lower()

    def get_char_rank(c):
        if c in [" ", "-"]:
            return 0
        if c.isalnum():
            return 1
        if c == '(':
            return 2
        return 3

    result = -999

    for j in range(min(len(a), len(b))):
        c1, c2 = a[j], b[j]
        rank1 = get_char_rank(c1)
        rank2 = get_char_rank(c2)

        # different char rank
        if rank1 != rank2:
            result = rank1 - rank2
            break

        # same rank, but different char
        if c1 != c2:
            result = -1 if c1 < c2 else 1
            break

    if result == -999:
      # one string contains the other, prioritize shorter one
      result = -1 if len(a) < len(b) else 1

    return result


def get_file_line_count():
    if os.name == "posix":
        return int(os.popen("wc -l < excluded_devices.csv").read()[:-1])
    elif os.name == "nt":
        return int(os.popen("type excluded_devices.csv | find /v /c \"\"").read()[:-1]) - 1
    else:
        raise Exception("Who are you?")


def get_dict_leaves_count(d) -> int:
    count = 0
    for key, value in d.items():
        if isinstance(value, dict):
            count += get_dict_leaves_count(value)
        else:
            count += 1
    return count


def get_dict_subitems_count(d) -> int:
  return len([key for key in d])


def get_quicklinks(d) -> str:
  quicklinks = ""
  for letter in d:
    quicklinks += f"[{letter}](#{letter.lower()}) "
  quicklinks += " \n*Links may only work in common browsers*\n\n"
  return quicklinks


def get_manufacturer_link(letter: str, index: int) -> str:
    if index != 0:
      return ""
    else:
      letter = letter.upper()
      return f" name=\"{letter}\""


data = OrderedDict()
output = ""
print(f"Reading CSV file in {os.getcwd()}")
with open("excluded_devices.csv", "r", encoding="utf-8") as file:
    reader = csv.reader(file, delimiter=",")
    row_count = get_file_line_count()
    print(f"Found {row_count} entries")
    if row_count < 1:
        raise Exception("Empty CSV file")
    next(reader)
    device_count = 0
    prev_name = None
    for row in reader:
        print("Entry:", row)
        manufacturer = row[2]
        manufacturer_letter = manufacturer[0].upper()
        model = row[0]
        model_name = row[1]

        # data cleanup
        # special case: merge 'Realme' into 'realme'
        if manufacturer == "Realme":
          manufacturer = "realme"
        # special case: merge 'vivo' into 'Vivo'
        if manufacturer == "vivo":
          manufacturer = "Vivo"

        # data[manufacturer_letter]
        prev_manufacturer_letter = data.get(manufacturer_letter, OrderedDict())
        # data[manufacturer_letter][manufacturer]
        prev_manufacturer = prev_manufacturer_letter.get(manufacturer, OrderedDict())
        # data[manufacturer_letter][manufacturer][model]
        prev_model_names = prev_manufacturer.get(model, [])
        if not prev_model_names.__contains__(model_name):
          prev_model_names.append(model_name)

        # data[manufacturer_letter][manufacturer][model] = [...]
        prev_manufacturer[model] = prev_model_names
        # data[manufacturer_letter][manufacturer] = {...}
        prev_manufacturer_letter[manufacturer] = prev_manufacturer
        # data[manufacturer_letter] = {...}
        data[manufacturer_letter] = prev_manufacturer_letter

output = f"<b>Total: {get_dict_leaves_count(data)}</b>\n\n" + get_quicklinks(data)

# iterate through manufacturer letters
for letter_key, letter_value in data.items():
    # iterate through manufacturers
    manufacturer_list = sorted([m for m, _ in letter_value.items()], key=cmp_to_key(lambda x, y: custom_compare(x, y)))
    manufacturer_index = 0
    for manufacturer_key in manufacturer_list:
        manufacturer_value = letter_value[manufacturer_key]
        model_count = get_dict_subitems_count(manufacturer_value)
        output += f"<details{get_manufacturer_link(letter_key, manufacturer_index)}>\n  <summary>{manufacturer_key} ({model_count})</summary>\n  <ul>\n"
        for model_key, model_names in manufacturer_value.items():
          output += "    <li>" + model_key + " [" + ", ".join(model_names) + "]</li>\n"
        output += "  </ul>\n</details>\n"
        manufacturer_index += 1

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
