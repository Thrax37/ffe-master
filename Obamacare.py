import sys

#get input
stdin = sys.argv[1].split(";")

#unpack input
rnd = stdin[0]
stdin.pop(0)
bot = stdin[0]
stdin.pop(0)
#find self
town = []
for player in stdin:
    if player.split("_")[0] == str(bot):
        town = player.split("_")
        break
#unpack town
healthy = town[1]
infected = town[2]

out = []

#get down to business
if int(infected) < int(healthy):
    out.append("ME")
if int(infected) >= int(healthy):
    out.append("CI")
if int(infected) >= 30:
    out.append("C")
else:
    out.append("T")

sys.stdout.write("".join(out))