import sys
a = sys.argv[1].split(';')
round = int(a[0])
myid = a[1]
players = {}
Sane = 0
Infected = 1
Dead = 2
InfectionRate = 3
ContagionRate = 4
LethalityRate = 5
MigrationRate = 6
for i in range(2,len(a)):
    b = a[i].split('_')
    players[b[0]]=map(int,b[1:])
output=''
if round<=4:output = ["OOO","OOO","OOO","OII"][round-1]
if round==50: output = "CCC"
mycontrate = players[myid][ContagionRate]
myfatrate = players[myid][LethalityRate]
myinfrate = players[myid][InfectionRate]
if myinfrate+mycontrate<5:
    output+="V"
    myfatrate-=2
    if round < 47: 
        output+="I"*(myfatrate/4)
        if myfatrate%4: output+="V"
else:
    if round < 47: 
        output+="I"*(myfatrate/4)
        if myfatrate%4: output+="V"
    output+="M"*(myinfrate/4)
    if round < 47: 
        output+="E"*(mycontrate/4)
output+="CCC"

print output[:3]