// Process input
var data = process.argv[2].split(';');
var round = data.shift()*1;
var id = data.shift()*1;
var playerCount = data.length;
var local = data.find(function(v) {
  return !v.indexOf(id+'_')}
).split('_');
local = {
  sane: local[1]*1,
  infected: local[2]*1,
  dead: local[3]*1,
  infectionRate: local[4]*1,
  contagionRate: local[5]*1,
  lethalityRate: local[6]*1,
  migrationRate: local[7]*1
};

// Determine response
var response = [];
for(var i=0;i<3;i++) {
  var model = {
    M: local.infectionRate * (local.sane > 0 ? 1 : 0.5),
    E: local.contagionRate * (local.sane > 0 ? 1 : 0.5),
    I: local.lethalityRate * (local.sane > 0 ? 1 : 2),
    V: local.infectionRate/4 + local.contagionRate/2 + local.lethalityRate/2,
    C: local.infected / Math.max(local.infectionRate, 1),
    B: local.migrationRate
  };
  var max = 'M';
  for(k in model) {
    if (model[k] > model[max] ) {
      max = k;
    } else if(model[k] == model[max]) {
      max = [max, k][Math.random()*2|0];
    }
  }
  response.push(max);

  // Refactor priorities
  if(max == 'M') {
    local.infectionRate -= 4;
  } else if(max == 'E') {
    local.contagionRate -= 8;
  } else if(max == 'I') {
    local.lethalityRate -= 4;
  } else if(max == 'V') {
    local.infectionRate -= 1;
    local.contagionRate -= 4;
    local.lethalityRate -= 2;
  } else if(max == 'C') {
    local.infected -= 10;
  } else if(max == 'B') {
    local.migrationRate -= 10;
  }
}

// Respond with actions
process.stdout.write(response.join(''));