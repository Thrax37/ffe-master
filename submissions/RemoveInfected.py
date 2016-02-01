# Remove as many of it's own infected as possible, preferably by curing, but quarantining if it's getting out of hand
# If not very many can be cured, takes preventative measures (B,E,M, or V)

from sys import argv

CMDS=3
C_RATE=10
E_RATE=.08
M_RATE=4
Q_RATE=30
V_RATE=(1,.04)

def find_me(args):
    for player in args:
        player=player.split('_')
        if int(player[0])==me:
            return player

def actions_available():
    global actions
    if len(actions) < CMDS:
        return True
    else:
        return False

def add_actions(new_actions):
    global actions
    actions = (actions + new_actions)[0:CMDS]

def get_remaining_infected(local_infected):
    global actions
    return local_infected - (Q_RATE*actions.count('Q')) - (C_RATE*actions.count('C'))

def too_many_infected(local_infected):
    max_infected = C_RATE*(CMDS+1) # If we can get down to 10 or less without quarantining, that's good
    if local_infected > max_infected:
        return True
    else: return False

def projection(infection_rate, remaining_infected, action):
    additional_M=0
    additional_E=0
    additional_V=0

    if action == "M":
        additional_M=1
    elif action == "E":
        additional_E=1
    else:
        additional_V=1

    M_level = M_RATE*(actions.count('M')+additional_M)
    E_level = E_RATE*(actions.count('E')+additional_E)
    V_level = (V_RATE[0]*(actions.count('V')+additional_V), V_RATE[1]*(actions.count('V')+additional_V))

    projection = infection_rate - M_level - V_level[0] + (remaining_infected * (contagion_rate - E_level - V_level[1])) 
    return int(projection)

def get_best_action(local_infected):
    global actions
    remaining_infected = get_remaining_infected(local_infected)

    # If we can leave no infected, do so
    if remaining_infected <= C_RATE and remaining_infected >= 0:
        return 'C'

    strategies = {'M':0, 'E':0, 'V':0,'C':min(remaining_infected,C_RATE)}

    pni = int(infection_rate + (remaining_infected*contagion_rate)) # predicted new infected
    strategies['M'] = pni - projection(infection_rate, remaining_infected, 'M')
    strategies['E'] = pni - projection(infection_rate, remaining_infected, 'E')
    strategies['V'] = pni - projection(infection_rate, remaining_infected, 'V')
    # any benefit to including P as an option? 

    #print(strategies)
    max_saved = 'C'
    for strat,saved in strategies.iteritems():
        if saved > strategies[max_saved]:
            max_saved=strat
        elif saved == strategies[max_saved]:
            #prioritize V because of it's extra benefit of reducind lethality_rate
            max_saved=max(strat,max_saved) 

    if strategies[max_saved] <= C_RATE/2:
        # can't save that many, just close borders instead
        selected_action = 'B'
    else: selected_action = max_saved
    return selected_action


args = argv[1].split(";")
round = int(args.pop(0))
me = int(args.pop(0))
actions = ""

my_town = find_me(args)

local_infected = int(my_town[2])
infection_rate = int(my_town[4])
contagion_rate = int(my_town[5])/100.

if round!=50 and too_many_infected(local_infected):
    # Things are getting out of hand, quarantine and consider preventative measures
    actions = ('Q'*(local_infected/Q_RATE))[0:CMDS]

    while actions_available():
        add_actions(get_best_action(local_infected))
else: actions='CCC'

print ''.join(sorted(actions)) # always cure first