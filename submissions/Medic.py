from random import *
commands = ""
while len(commands) < 3:
    chance = random()
    if chance < .5: commands += "V"
    elif chance < .66: commands += "I"
    elif chance < .84: commands += "E"
    else: commands += "M"

print(commands)