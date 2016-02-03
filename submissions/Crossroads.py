import sys
import random
import itertools
def sample_wr(population, k):
    "Chooses k random elements (with replacement) from a population"
    n = len(population)
    _random, _int = random.random, int  # speed hack
    return [population[_int(_random() * n)] for i in itertools.repeat(None, k)]
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
worldpopulation = 0
for i in range(2,len(a)):
    b = a[i].split('_')
    players[b[0]]=map(int,b[1:])
    worldpopulation += (int(b[1])+int(b[2]))*int(b[7])/100
output = ""
if round == 1:
    output="BM"
    if players[myid][Infected]>6: output+="C"
    else: output+="E"
if round == 50: 
    if players[myid][Infected] > 20: output = "CCC"
    elif players[myid][Infected]> 6: output = "CTC"
    else: output = "TTC"
if round == 48 and players[myid][Infected] > 45 and players[myid][InfectionRate]>12:
    output = "MMM"
if round == 49 and players[myid][Infected] > 30:
    output = "CCC"
if (round+1)%5==0:
    if players[myid][Sane]==0 or players[myid][Infected]/players[myid][Sane] > 2: output+="I"*(players[myid][LethalityRate]/4)
    output+="M"*(players[myid][InfectionRate]/4)
    output+="C"*max((players[myid][Infected]/10),1)
if players[myid][InfectionRate] < 8 and players[myid][ContagionRate] < 20 and players[myid][Sane]+min(players[myid][Infected]/5,60)>players[myid][Infected] and (round+2)%5==0:
    output+="C"*max((players[myid][Infected]/10),1)
    players[myid][Infected] -= min(max((players[myid][Infected]/10),1)*10,players[myid][Infected])
if players[myid][Sane] > players[myid][Infected] > 30: 
    output +="Q"
    players[myid][Infected] -= min(players[myid][Infected],30)
if players[myid][Sane] > players[myid][Infected] > 20:
    output+="CC"
    players[myid][Infected] -= min(players[myid][Infected],20)
if (players[myid][Sane] > 2*players[myid][Infected] > 20):
    output+="C"
    players[myid][Infected] -= min(players[myid][Infected],10)
if round <= 5 and players[myid][Infected] > 10:
    output+="C"
    players[myid][Infected] -= min(players[myid][Infected],10)
if 25 <= players[myid][Infected] < 40 and players[myid][InfectionRate]<10:# and players[myid][ContagionRate]*(players[myid][Infected]-20)/100 < 10:
    output+="CCC"

if players[myid][InfectionRate]-players[myid][ContagionRate]>10: output+="M"
if players[myid][ContagionRate]-players[myid][InfectionRate]>20: output+="E"
population = []
population +=["I" for i in range(int(1.15**players[myid][LethalityRate]))]
if players[myid][Sane]<10 or players[myid][Infected]-players[myid][Sane]>10: population+=["I" if players[myid][LethalityRate]>8 else "V" for i in range(players[myid][InfectionRate])]
if players[myid][Sane]+players[myid][Infected]>10 and (players[myid][Sane]>15 or players[myid][LethalityRate]<10): population += ["M" if players[myid][InfectionRate] > 6 else "V" for i in range(2*max(players[myid][InfectionRate]*players[myid][Sane]/100,int((1.15+0.002*(50-round))**players[myid][InfectionRate])))]
if players[myid][Sane]+players[myid][Infected]>10 and (players[myid][Sane]>15 or players[myid][LethalityRate]<10): population += ["E" if players[myid][ContagionRate] > 10 else "V" for i in range(max(min(players[myid][Sane],players[myid][ContagionRate]*players[myid][Infected]/100),int(1.15**players[myid][ContagionRate])))]
if players[myid][InfectionRate]+players[myid][ContagionRate]<15: population += ["C" for i in range(players[myid][Infected])]
if players[myid][Infected] < 10: population += ["WV" for i in range(int(1.05**round))]
output += ''.join(sample_wr(population,3))
print output[:3]