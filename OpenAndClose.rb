#You can copy this code if you want. Not specific to my strategy.
PlayerId = 0
Healthy = 1
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
if round < 30
  commands += me[MigrationRate] == 100 ? (me[InfectionRate] <= 1 ? "V" : "M") : "O"
  commands += me[LethalityRate] <= 2 ? "V" : "I"
  commands += me[ContagionRate] <= 4 ? "V" : "E"
elsif round < 50
  commands += me[MigrationRate] == 0 ? "V" : "B"
  commands += me[LethalityRate] < 20 ? "C" : "I"
  commands += me[ContagionRate] <  5 ? "C" : "E"
else
  commands = "CCC"
end

print commands
$stdout.flush