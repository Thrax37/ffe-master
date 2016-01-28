# Parsing code
from sys import argv

args = argv[1].split(";")

n = int(args[0])
pid = int(args[1])
dic = ["pid","healthy","infected","dead","infection","contagion","lethality","migration"]
players = []
for p in args[2:]:
    players += [{dic[i]:int(p.split("_")[i]) for i in range(len(p.split("_")))}]
    if int(p.split("_")[0]) == pid:
        me = players[-1]

# Bot code

actions = ""
nextInfected = me["infected"]*me["contagion"]/100 + me["infection"] + me["infected"] - me["infected"]*me["lethality"]/100
if n%5 == 4:
    nextInfected *= 1.5

if n == 1:
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
elif n == 50:
    print("CCC")
    exit()


if nextInfected*1.2 > 30:
    if me["infected"] >= 15:
        actions += "Q"
        me["infected"] -= 30
    else:
        actions += "C"
        me["infected"] -= 10
elif me["infection"] > 0:
    actions += "M"
    me["infection"] -= 4
elif me["contagion"] >= 6:
    actions += "E"
    me["contagion"] -= 8
elif me["infected"] > 0:
    actions += "C"
    me["infected"] -= 10
else:
    actions += "E"
    me["contagion"] -= 8

if me["infection"] >= 3:
    actions += "M"
    me["infection"] -= 4
elif me["infected"] >= 7 :
    actions += "C"
    me["infected"] -= 10
elif me["infection"] > 0 and me["contagion"] >= 3:
    actions += "V"
    me["infection"] -= 1
    me["contagion"] -= 4
elif me["contagion"] >= 6:
    actions += "E"
    me["contagion"] -= 8
elif me["infection"] > 0:
    actions += "M"
    me["infection"] -= 4
elif me["infected"] > 0:
    actions += "C"
    me["infected"] -= 10
else:
    actions += "E"
    me["contagion"] -= 8

if me["infection"] >= 3:
    actions += "M"
    me["infection"] -= 4
elif me["infected"] >= 7 :
    actions += "C"
    me["infected"] -= 10
elif me["infection"] > 0 and me["contagion"] >= 3:
    actions += "V"
    me["infection"] -= 1
    me["contagion"] -= 4
elif me["contagion"] >= 6:
    actions += "E"
    me["contagion"] -= 8
elif me["infection"] > 0:
    actions += "M"
    me["infection"] -= 4
elif me["infected"] > 0:
    actions += "C"
    me["infected"] -= 10
else:
    actions += "E"
    me["contagion"] -= 8

if actions[-2:] == "VV":
    actions = actions[0] + "ME"
print(actions)