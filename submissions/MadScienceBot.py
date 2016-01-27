import sys, copy
import itertools

mults = {'mig_rate': -15, 'let_rate': -15, 'dead': -20, 'inf_rate': -20, 'sane': 0, 'infected': 60, 'con_rate': -30, 'id': 0}
def get_score(player_data):
    score = 0
    for k in player_data:
        score += player_data[k] * mults[k] / 100.
    return score


def add_rates(player_data):
    #Infection
    no_sane_converted = player_data["sane"]*player_data["inf_rate"]/100.
    player_data["infected"] += no_sane_converted
    player_data["sane"] -= no_sane_converted
    #Contagion
    no_sane_converted = player_data["con_rate"]
    player_data["infected"] += no_sane_converted
    player_data["sane"] -= no_sane_converted
    #Extinction
    no_killed = player_data["infected"]*player_data["let_rate"]/100.
    player_data["dead"] += no_killed
    player_data["infected"] -= no_killed

def correct(data):
    if round % 5 == 4:
        data["sane"] += int(data["sane"])/2
        data["infected"] += int(data["infected"])/2
    data["inf_rate"] += 2
    data["con_rate"] += 5
    data["let_rate"] += 5

args = sys.argv[1].split(";")
round = int(args[0])
self_id = int(args[1])
player_data = [map(int, player.split("_"))for player in args[2:]]
player_data = [dict(zip(("id", "sane", "infected", "dead", "inf_rate", "con_rate", "let_rate", "mig_rate"), player)) for player in player_data]
self_data = [player for player in player_data if player["id"] == self_id][0]

f = open("MadScienceBot.txt", "a")
f.write("\n")
f.write(`round`+"\n")
f.write("INPUT: "+`self_data`+"\n")

def m(p): p["inf_rate"] -= 4
def e(p): p["con_rate"] *= 92/100.
def i(p): p["let_rate"] -= 4
def v(p): p["inf_rate"] -= 1; p["con_rate"]-=4;p["let_rate"]-=2
def c(p): x=min(p['infected'], 10); p['infected']-=x; p['sane']+=x
def q(p): x=min(p['infected'], 30); p['infected']-=x; p['dead']+=x
def o(p): p["mig_rate"] += 10
def b(p): p["mig_rate"] -= 10

out = ""
instructions = {"M": m,
                "E": e,
                "I": i,
                "V": v,
                "C": c,
                "Q": q,
                "O": o,
                "B": b}

def run_inst(new_data, inst_id, i):
    inst = instructions[inst_id]
    if i != 2:
        inst(new_data)
        for j in new_data: new_data[j] = max(0, int(new_data[j]))
        #f.write("%s %s %s\n"%(inst_id, get_score(new_data), new_data))
    else:
        inst(new_data)
        for j in new_data: new_data[j] = max(0, int(new_data[j]))
        correct(new_data)
        add_rates(new_data)
        for j in new_data: new_data[j] = max(0, int(new_data[j]))
        #f.write("%s %s %s\n"%(inst_id, get_score(new_data), new_data))
    return new_data

def run_3_insts(self_data, insts):
    new_data = copy.copy(self_data)
    for i, inst in enumerate(insts):
        run_inst(new_data, inst, i)
    return get_score(new_data)

scores = {}
for combo in itertools.permutations(instructions.keys(), 3):
    joined = "".join(combo)
    score = run_3_insts(self_data, joined)
    scores[score] = joined
#print scores
out = scores[max(scores)]

if round == 50:
    out = "CCC"

f.write(out+"\n")
print out