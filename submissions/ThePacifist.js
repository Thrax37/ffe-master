// Process input
var data = process.argv[2].split(';');
var round = data.shift()*1;

// Respond with actions
process.stdout.write(round == 1 ? 'OOO' : 'PPP');