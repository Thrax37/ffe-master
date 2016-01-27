# parsing code
args = argv[1].split(";")

n = int(args[0])
pid = int(args[1])
dic = ["pid","healthy","infected","dead","infection","contagion","lethality","migration"]
players = []
for p in args[2:]:
    players += [{dic[i]:int(p.split("_")[i]) for i in range(len(p.split("_")))}]
    if int(p.split("_")[0]) == pid:
        me = players[-1]

# bot code

actions =""

if n == 50:
    print("CCC")
    exit()
elif n == 1:
    actions += "B"
    if me["lethality"] <= 6:
        actions += "WI"
    else:
        actions += "II"
    print(actions)
    exit()

if me["lethality"] >= 7:
    actions += "III"
elif me["lethality"] >= 1:
    actions += "WII"
else:
    actions += "WWI"
print(actions)