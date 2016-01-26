const argv = String(process.argv),
    data = argv.split(";"),
    round = data[0],
    id = Number(data[1]),
    info = data[id + 1].split("_"),
    sane = info[1],
    infected = info[2],
    dead = info[3],
    infectionRate = info[4],
    contagionRate = info[5],
    lethalityRate = info[6],
    migrationRate = info[7]

var moves = 3
function exec(a) {
  process.stdout.write(a)
}
if(migrationRate >= 10) {
  exec("B")
}
if (infectionRate >= 8) {
  exec("MQ")
  moves-=2;
} else if(contagionRate >= 16) {
  exec("EC")
  moves-=2;
} else if(lethalityRate >= 8) {
  exec("IV")
  moves--;
} else {
  exec("B");
  moves--;
}

if (sane / 3 > infected + dead) {
  exec("Q")
  moves--;
}
if(moves > 0) {
  exec("B")
}