import sys, copy

mults = {'mig_rate': 0, 'let_rate': 0, 'dead': 1, 'inf_rate': 15, 'sane': 20, 'infected': 5, 'con_rate': 10, 'id': 0}
def get_score(player_data):
    score = 0
    for k in player_data:
        score += player_data[k] * mults[k] / 100.
    return score


def add_rates(player_data):
    #Infection
    no_sane_converted = player_data["inf_rate"]/100.
    player_data["infected"] += no_sane_converted
    player_data["sane"] -= no_sane_converted
    #Contagion
    no_sane_converted = int(player_data["infected"]*player_data["con_rate"]/100.)
    player_data["infected"] += no_sane_converted
    player_data["sane"] -= no_sane_converted
    #Extinction
    no_killed = player_data["let_rate"]/100.
    player_data["dead"] += no_killed
    player_data["infected"] -= no_killed

args = sys.argv[1].split(";")
round = int(args[0])
self_id = int(args[1])
player_data = [map(int, player.split("_"))for player in args[2:]]
player_data = [dict(zip(("id", "sane", "infected", "dead", "inf_rate", "con_rate", "let_rate", "mig_rate"), player)) for player in player_data]
self_data = [player for player in player_data if player["id"] == self_id][0]

self_data["inf_rate"] += 2
self_data["con_rate"] += 5
self_data["let_rate"] += 5

if round % 5 == 0:
    self_data["sane"] += int(self_data["sane"])/2
    self_data["infected"] += int(self_data["infected"])/2

def m(p): p["inf_rate"] -= 4
def e(p): p["con_rate"] *= 92/100.
def i(p): p["let_rate"] -= 4
def v(p): p["inf_rate"] -= 1; p["con_rate"]-=4;p["let_rate"]-=2
def c(p): x=min(p['infected'], 10); p['infected']-=x; p['sane']+=x
def q(p): x=min(p['infected'], 30); p['infected']-=x; p['dead']+=x

out = ""
instructions = {"M": m,
                "E": e,
                "I": i,
                "V": v,
                "C": c,
                "Q": q}
for i in range(3):
    scores = {}
    results = {}
    for inst_id in instructions:
        temp = copy.copy(self_data)
        inst = instructions[inst_id]
#        print temp[inst[0]], inst[1]
        inst(temp)
        for i in temp:
            temp[i] = max(0, int(temp[i]))
        add_rates(temp)
        for i in temp:
            temp[i] = max(0, int(temp[i]))
        scores[get_score(temp)] = inst_id
        results[inst_id] = temp
    inst = scores[min(scores)]
    self_data = results[inst]
    out += inst
print out