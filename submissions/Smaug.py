# "I am fire, I am death"
# Smaug has two goals: hoard gold and bring death...
#    and this world seems to be all out of gold

from sys import argv
args = argv[1].split(";")

round = int(args.pop(0))
me = int(args.pop(0))

if round==50: # can't cause more death on the last round, might as well infect
    print('TTT')

def predict_infected(infected, infection_rate, contagion_rate):
    i = infected + infection_rate
    return i + int(i*contagion_rate)

def predict_dead(infected, lethality_rate):
    return int(infected*lethality_rate)

strategies = {'WWW':0, 'WWD':0, 'WDD':0, 'DDD':0}
for player in args:
    player=player.split('_')
    healthy=int(player[1])
    infected=int(player[2])
    infection_rate=int(player[4])
    contagion_rate=int(player[5])/100.
    lethality_rate=int(player[6])/100.

    if round%5==0:
        healthy+=healthy/2
        infected+=infected/2

    pi_old = predict_infected(infected, infection_rate, contagion_rate)
    pd_old = predict_dead(pi_old, lethality_rate)

    for strat in strategies:
        ir_new = infection_rate + 3
        lr_new = lethality_rate + (strat.count('W')*.02) 
        cr_new = contagion_rate + (strat.count('D')*.02) 

        pi_new = predict_infected(infected, ir_new, cr_new)
        pd_new = predict_dead(pi_new, lr_new)

        increase = pd_new - pd_old

        strategies[strat]+=increase

print max(strategies, key=strategies.get)