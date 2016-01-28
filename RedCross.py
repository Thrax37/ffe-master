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
output="PPPPP"
if round<=4:output = ["OOO","OOO","OOO","OII"][round-1]
elif round==50: output = "CCC"
else: output = output[:2-3*round%5]+"I"+output[2-3*round%5:]
print output[:3]