from functools import cmp_to_key

def custom_compare(a, b):
    a = a.lower().split(",")[:-1]
    b = b.lower().split(",")[:-1]

    def get_char_rank(c):
        if c in [" ", "-"]:
            return 0
        if c.isalnum():
            return 1
        if c == '(':
            return 2
        return 3

    result = -999

    for i in range(4):
      if a[i] == b[i]:
        # entry with same column value, check next column
        continue
      # substrings are different, this round will definitely return a result

      for j in range(min(len(a[i]), len(b[i]))):
          c1, c2 = a[i][j], b[i][j]
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
          # same rank, same char, continue with next char

      if result != -999:
          break
      # one string contains the other, prioritize shorter one
      result = -1 if len(a[i]) < len(b[i]) else 1
      break

    return result


def main():
  excluded_devices_list = "../app/src/main/res/raw/excluded_devices.csv"

  with open(excluded_devices_list, "r", encoding="utf-8") as f:
      print("Loading current file content")
      lines = f.readlines()

  header = lines[0]
  content = lines[1:]
  sorted_content = sorted(content, key=cmp_to_key(lambda x, y: custom_compare(x, y)))

  with open(excluded_devices_list, "w", encoding="utf-8") as f:
      print("Writing sorted file content")
      f.writelines([header] + sorted_content)

if __name__ == "__main__":
    main()
