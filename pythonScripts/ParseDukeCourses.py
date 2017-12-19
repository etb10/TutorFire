import sys
import re

invalid_prefixes = set(["NOTE:", "MW", "TTH", "MF", "M", "T", "W", "TH", "F",
                        "MWF", "WF", "MTWTHF", "WTH", "TBA", "STAFF", "SPRING"])
number_pattern = r"^\d*S$"

def isCourseLine(line):
    split_line = line.split()
    if (len(split_line) >= 2):
        prefix = split_line[0]
        return (goodPrefix(prefix))

def goodPrefix(prefix):
    match = re.search(number_pattern, prefix)
    return (prefix.isupper() and prefix not in invalid_prefixes and not match and len(prefix) > 1)


def parseRawCourseFile(file):
    ret = []
    with open(file, 'r') as f:
        for line in f:
            print(line)
            if isCourseLine(line):
                split_line = line.split()
                ret.append(" ".join([split_line[0], split_line[1]]))
    ret = sorted(list(set(ret)))
    return ret

def writeToFile(str_arr, file):
    str = "\n".join(str_arr)
    with open(file, 'w') as f:
        f.write(str)


def main(args):
    in_file = args[0]
    out_file = args[1]
    result = parseRawCourseFile(in_file)
    writeToFile(result, out_file)

if __name__ == '__main__':
    main(sys.argv[1:])
