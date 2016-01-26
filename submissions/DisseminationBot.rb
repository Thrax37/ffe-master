#You can copy this code if you want. Not specific to my strategy.
PlayerId = 0
Sane = 1
Infected = 2
Dead = 3
InfectionRate = 4
ContagionRate = 5
LethalityRate = 6
MigrationRate = 7

a = ARGV[0].split ';'
round = a.shift.to_i
my_id = a.shift.to_i
players = a.map{|s|s.split('_').map{|str| str.to_i}}

my_index = players.index{|p|
    p[PlayerId] == my_id
}
me = players[my_index]

#strategy specific code starts here.

commands = ""
commands += 'C' if me[Infected] >= 10
commands += 'C' if me[Infected] >= 20
commands += 'C' if me[Infected] >= 30
commands += 'M' if me[InfectionRate] >= 4 and commands.length < 3
commands += 'D' while commands.length < 3

print commands
$stdout.flush