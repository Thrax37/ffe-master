import sys
import random
import math


def main():
    id = int(get_player_id(sys.argv[1]))
    stats = get_player_stats(sys.argv[1], id)
    round = int(get_round(sys.argv[1]))

    if id == -1 or stats == None or round == -1:
        # Something is wrong here. RED ALERT! Close all possible entry routes and set 
        # quarantine levels to maximum!
        print("BQQ")
        sys.exit(1)

    if round == 1:
        # remove migration, cure some infected, and remove some danger
        print("BCM")
    elif round % 5 == 4:
        # Rounds 4, 9, 14 etc. One before repopulation. We want as many Healthy and as 
        # few Infected as possible to reproduce. Prioritise curing infected, because that
        # maximises Healthy for reproduction. If that's not possible, quarantine them.
        quarantine = math.ceil(int(stats['infected']) / 30)
        cure = math.ceil(int(stats['infected']) / 10)
        if cure <= 3:
            # we can deal with all our infections within 3 cures
            output = "C" * cure
            for i in range(3 - cure):
                # got moves left? Great, remove some danger.
                output += get_random_science()
            print(output)
        elif quarantine <= 3:
            # we can deal with all our infections within 3 quarantines
            output = "Q" * quarantine
            for i in range(3 - quarantine):
                # got moves left? Great, remove some danger.
                output += get_random_science()
            print(output)
        else:
            # We can't deal with all the infected in one round, so deal with some. Yes, we
            # don't get rid of as many as we could here, but we're about to reproduce so
            # we want Healthies in the next round.
            print("QQC")
    else:
        output = ""
        if int(stats['infected']) <= 10:
            # we can deal with all our infections by curing them
            output += "C"
        elif int(stats['infected']) <= 30:
            # we can deal with all our infections by quarantining them
            output += "Q"
        elif int(stats['infected']) >= int(stats['healthy']) * 0.5:
            # we're getting overrun with infected people, get rid of some
            output = "QCC"

        for i in range(3 - len(output)):
            # if we haven't used all our moves, we can remove some danger factors
            output += get_random_science()

        print(output)


def get_random_science():
    return random.choice(["M", "E", "I", "V"])


def get_player_id(args):
    splat = args.split(";")
    return splat[1] if len(splat) >= 2 else -1


def get_player_stats(args, id):
    splat = args.split(";")
    players_data = [x for x in splat if "_" in x]
    my_data = [y for y in players_data if y.split("_")[0] == str(id)]
    data_splat = my_data[0].split("_")

    if len(data_splat) == 8:
        # Id, Healthy, Infected, Dead, InfRate, ConfRate, LethRate, MigRate
        return {
            'healthy': data_splat[1],
            'infected': data_splat[2],
            'dead': data_splat[3],
            'inf_rate': data_splat[4],
            'conf_rate': data_splat[5],
            'leth_rate': data_splat[6],
            'mig_rate': data_splat[7]
        }
    else:
        return None


def get_round(args):
    splat = args.split(";")
    return splat[0] if len(splat) >= 1 else -1


if __name__ == "__main__":
    main()