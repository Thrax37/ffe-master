# Parsing code
from sys import argv

args = argv[1].split(";")

n = args[0]
pid = args[1]
dic = ["pid","healthy","infected","dead","infection","contagion","lethality","migration"]
players = dictionary()
for p in range(len(args)-2):
    players += {dic[i]:p.split("_")[i] for i in range(p.split("_"))}
    if p.split("_")[0] == pid:
        me = players[-1]

# Bot code

actions = ""
nextInfected = me["infected"]*me["contagion"]/100 + me["infection"] + me["infected"] - me["infected"]*me["lethality"]/100
if n%5 == 4:
    nextInfected *= 1.5

if n == 0:
    actions += "BM"
    if nextInfected*1.3 > 10:
        actions += "C"
    elif me["infection"] > 6:
        actions += "M"
    elif me["infection"] > 4:
        actions += "V"
    else:
        actions += "E"
    print(actions)
    exit()

if nextInfected*1.2 > 30:
    actions += "Q"
else:
    actions += "M"

if me["infection"] >= 4:
    actions += "M"
else:
    actions += "E"

if me["infection"] >= 8:
    actions += "M"
else:
    actions += "E"

print(actions)