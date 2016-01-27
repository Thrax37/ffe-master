import sys
import random

def main():
    print("{0}{1}{2}".format("Q", get_random_science(), get_random_action()))

def get_random_action():
    return random.choice(["M", "E", "I", "V", "C", "Q", "O", "B", "T", "W", "D", "P"])

def get_random_science():
    return random.choice(["M", "E", "I", "V", "C"])


if __name__ == "__main__":
    main()