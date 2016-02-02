package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import players.*;

public class Game {
	private static Player[] players = {
		new AllOrNothing(),
		new BioterroristBot(), // D
		new Crossroads(),
		new CullBot(),
		new CureThenQuarantine(),
		new DisseminationBot(),
		new FamilyValues(),
		new Graymalkin(),
		new InfectedHaven(), 
		new InfectedTown(), // I
		new InfectionBot(), // I
		new Israel(),
		new Madagascar(),
		new MadScienceBot(),
		new Medic(),
		new MedicBot(),
		new Mooch(),
		new Obamacare(),
		new OpenAndClose(),
		new PassiveBot(),
		new PFC(), // I (C)
		new Piecemeal(),
		new PureBot(),
		new RedCross(),
		new RemoveInfected(),
		new Researcher(),
		new Salt(),
		new Socialist(), // I
		new Smaug(), // D
		new Strategist(),
		new Terrorist(), // I
		new TheCure(),
		new TheKeeper(),
		new ThePacifist(), // I
		new Triage(),
		new TrumpBot(),
		new UndecidedBot(),
		new WeaponOfMassDissemination(),
		new WICKED(),
		new XenoBot(),
		new ZombieState() // I(C)
	};
	
	// Game Parameters
	private static final int ROUNDS = 50;
	private static final int GAMES = 10;
	
	// Console
	private static final boolean DEBUG = false;
	private static final boolean GAME_MESSAGES = false;
	private static final boolean GLOBAL_MESSAGES = false;
	
	private static final int START_SANE = 99;
	private static final int START_INFECTED = 1;
	private static final int START_DEAD = 0;
	private static final int START_INFECTION = 2;
	private static final int START_CONTAGION = 5;
	private static final int START_LETHALITY = 10;
	private static final int START_MIGRATION = 5;
		
	private static final int BIRTH_ROUND = 5;	
	
	private static final int MUTATION_INFECTION = 2;
	private static final int MUTATION_CONTAGION = 5;
	private static final int MUTATION_LETHALITY = 5;
	
	private static final int MICROBIOLOGY_INFECTION = 4;
	private static final int EPIDEMIOLOGY_CONTAGION = 8;
	private static final int IMMUNOLOGY_LETHALITY = 4;
	private static final int VACCINATION_INFECTION = 1;
	private static final int VACCINATION_CONTAGION = 4;
	private static final int VACCINATION_LETHALITY = 2;
	
	private static final int CURE_INFECTED = 10;
	private static final int QUARANTINE_INFECTION = 30;
	
	private static final int OPEN_MIGRATION = 10;
	private static final int CLOSE_MIGRATION = 10;
	
	private static final int BIOTERRORISM_INFECTED = 4;
	
	private static final int DISSEMINATION_INFECTION = 1;
	private static final int DISSEMINATION_CONTAGION = 2;
	
	private static final int WEAPONIZATION_INFECTION = 1;
	private static final int WEAPONIZATION_LETHALITY = 2;
	
	private static final int PACIFICATION_INFECTION = 1;
	private static final int PACIFICATION_CONTAGION = 1;
	private static final int PACIFICATION_LETHALITY = 1;
	
	private final List<State> states = new ArrayList<State>();
	private int round = 0;
	
	public Game() {
		for (int i = 0; i < players.length; i++) {
			players[i].setId(i);
		}
	}
	
	public static void main(String... args) {
		
		List<List<Score>> totalScores = new ArrayList<>();
		
		// Starting
		for (int i = 0; i < GAMES; i++) {
			totalScores.add(new Game().run());
		}
		
		// Scores
		Map<Player, List<Score>> playerScores = new HashMap<>();
		for (List<Score> scores : totalScores) {
			for (Score score : scores) {
				if (playerScores.get(score.getPlayer()) == null) {
					playerScores.put(score.getPlayer(), new ArrayList<>());
				}
				playerScores.get(score.getPlayer()).add(score);
			}
		}
		
		List<Score> finalScores = new ArrayList<>();
		for (Player player : playerScores.keySet()) {
			int healthy = 0;
			int infected = 0;
			int dead = 0;
			
			for (Score score : playerScores.get(player)) {
				healthy += score.getHealthy();
				infected += score.getInfected();
				dead += score.getDead();
			}
			
			healthy = Math.floorDiv(healthy, playerScores.get(player).size());
			infected = Math.floorDiv(infected, playerScores.get(player).size());
			dead = Math.floorDiv(dead, playerScores.get(player).size());
			
			finalScores.add(new Score(player, healthy, infected, dead));
		}
		
		//sort descending
		Collections.sort(finalScores, Collections.reverseOrder());
		
		System.out.println("################################");
		for (int i = 0; i < finalScores.size(); i++) {
			Score score = finalScores.get(i);
			System.out.println(i+1 + ". " + score.print());
		}
	}	
	
	public List<Score> run() {
			
		if (GLOBAL_MESSAGES) 
			System.out.println("Starting a new game...");
		
		this.initialize();
		
		if (GLOBAL_MESSAGES) 
			System.out.println("Game begins.");
							
		for (round = 1; round <= ROUNDS; round++) {
			if (GLOBAL_MESSAGES) {
				System.out.println("======== Round : " +  round + " ========");
			}
			
			Collections.shuffle(states);
			
			if (!makeTurns()) break; //break if only no player left
		}
	
		return printResults();
	}
	
	private void initialize() {		
		
		for (int i = 0; i < players.length; i++) {
			try {
				if (GAME_MESSAGES) System.out.println("Player \"" + players[i].getDisplayName() + "\" added.");
				State State = new State(i, players[i], START_SANE, START_INFECTED, START_DEAD, START_INFECTION, START_CONTAGION, START_LETHALITY, START_MIGRATION);
				states.add(State);
			} catch (Exception e) {
				if (DEBUG) {
					System.out.println("Exception in initialize() by " + players[i].getDisplayName());
					e.printStackTrace();
				}
			}
		}
	}	
	
	private boolean makeTurns() {
		
		// Phase 1 : Mutation
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 1 : Mutation ---");
		mutation();
				
		// Phase 2 : Reproduction
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 2 : Reproduction ---");
		reproduction();
		
		// Phase 3 : Migration
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 3 : Migration ---");
		migration();
		
		// Phase 4 : Infection
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 4 : Infection ---");
		infection();
		
		// Phase 5 : Contagion
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 5 : Contagion ---");
		contagion();
		
		// Phase 6 : Extinction
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 6 : Extinction ---");
		extinction();	
		
		// Phase 7 : Player Turn
		if (onePlayerLeft()) return false;
		if (DEBUG) System.out.println("--- Phase 7 : Players ---");
		for (State state : states) {
			
			if (state.isAlive()) {
				Player owner = state.getOwner();
				try {
					String request = round + ";" + owner.getId() + generateArgs();
					String response = state.getCommand(request);
					if (DEBUG) {
						System.out.println("Request : " + request);
						System.out.println("Response : " + response);
					}
					
					if (response.length() < 3) {
						throw new Exception("Invalid response length : " + response.length());
					}
					
					for (int i = 0; i < 3; i++) {
						switch (response.charAt(i)) {
							case 'M': executeMicrobiology(state); break;
							case 'E': executeEpidemiology(state); break;
							case 'I': executeImmunology(state); break;
							case 'V': executeVaccination(state); break;
							case 'C': executeCure(state); break;
							case 'Q': executeQuarantine(state); break;
							case 'O': executeOpenBorders(state); break;
							case 'B': executeCloseBorders(state); break;
							case 'T': executeBioterrorism(state); break;
							case 'D': executeDissemination(state); break;
							case 'W': executeWeaponization(state); break;
							case 'P': executePacification(state); break;
							case 'N': executeWait(state); break;
							default : executeWait(state); break;
						}
					}			
					
				} catch (Exception e) {
					if (DEBUG) {
						System.out.println("Exception in makeTurns() by " + owner.getDisplayName());
						e.printStackTrace();
					}
				}
			
				
				if (onePlayerLeft()) return false;
			}
		}
		
		if (GLOBAL_MESSAGES) printStatus();
		
		return true;
	}
	
	private boolean onePlayerLeft() {
		
		int alive = 0;
		for (State state : states) {
			alive += state.isAlive() ? 1 : 0;
		}
				
		return alive <= 1;
	}
	
	private void executeMicrobiology(State state) {
		state.setInfectionRate(Math.max(0, state.getInfectionRate() - MICROBIOLOGY_INFECTION));
		
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " reduced local infection rate by " + MICROBIOLOGY_INFECTION + " (" + state.getInfectionRate() + ")");
	}
	
	private void executeEpidemiology(State state) {
		state.setContagionRate(Math.max(0, state.getContagionRate() - EPIDEMIOLOGY_CONTAGION));
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " reduced local contagion rate by " + EPIDEMIOLOGY_CONTAGION + "% (" + state.getContagionRate() + "%)");
	}
	
	private void executeImmunology(State state) {
		state.setLethalityRate(Math.max(0, state.getLethalityRate() - IMMUNOLOGY_LETHALITY));
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " reduced local lethality rate by " + IMMUNOLOGY_LETHALITY + "% (" + state.getLethalityRate() + "%)");
	}
	
	private void executeVaccination(State state) {
		state.setInfectionRate(Math.max(0, state.getInfectionRate() - VACCINATION_INFECTION));
		state.setContagionRate(Math.max(0, state.getContagionRate() - VACCINATION_CONTAGION));
		state.setLethalityRate(Math.max(0, state.getLethalityRate() - VACCINATION_LETHALITY));	
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " reduced local infection rate by " + VACCINATION_INFECTION + " (" + state.getInfectionRate() + "), local contagion rate by "
		+ VACCINATION_CONTAGION + "% (" + state.getContagionRate() + "%), local lethality rate by "
		+ VACCINATION_LETHALITY + "% (" + state.getLethalityRate() + "%)");
	}
	
	private void executeCure(State state) {
		int cured = Math.max(0, Math.min(state.getInfected(), CURE_INFECTED));
		state.setHealthy(state.getHealthy() + cured);
		state.setInfected(state.getInfected() - cured);
		
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " cured " + cured + " infected");
	}
	
	private void executeQuarantine(State state) {
		int quarantined = Math.max(0, Math.min(state.getInfected(), QUARANTINE_INFECTION));
		state.setInfected(state.getInfected() - quarantined);
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " quarantined " + quarantined + " infected");
	}
	
	private void executeOpenBorders(State state) {
		state.setMigrationRate(Math.min(100, state.getMigrationRate() + OPEN_MIGRATION));
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " increased migration rate by " + OPEN_MIGRATION + "% (" + state.getMigrationRate() + "%)");
	}
	
	private void executeCloseBorders(State state) {
		state.setMigrationRate(Math.max(0, state.getMigrationRate() - CLOSE_MIGRATION));		

		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " decreased migration rate by " + OPEN_MIGRATION + "% (" + state.getMigrationRate() + "%)");
	}
	
	private void executeBioterrorism(State state) {
		for (State s : states) {
			int infected = Math.min(s.getHealthy(), BIOTERRORISM_INFECTED);
			s.setHealthy(s.getHealthy() - infected);
			s.setInfected(s.getInfected() + infected);
		}
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " launched a bioweapon : +" + BIOTERRORISM_INFECTED + " global infected");
	}
	
	private void executeDissemination(State state) {
		for (State s : states) {
			s.setInfectionRate(s.getInfectionRate() + DISSEMINATION_INFECTION);
			s.setContagionRate(Math.min(100, s.getContagionRate() + DISSEMINATION_CONTAGION));
		}
		
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " increased global infection rate by " + DISSEMINATION_INFECTION + " and global contagion rate by " + DISSEMINATION_CONTAGION + "%");
	}
	
	private void executeWeaponization(State state) {
		for (State s : states) {
			s.setInfectionRate(s.getInfectionRate() + WEAPONIZATION_INFECTION);
			s.setLethalityRate(Math.min(100, s.getLethalityRate() + WEAPONIZATION_LETHALITY));
		}
	
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " increased global infection rate by " + WEAPONIZATION_INFECTION + " and global lethality rate by " + WEAPONIZATION_LETHALITY + "%");
	}
	
	private void executePacification(State state) {
		for (State s : states) {
			s.setInfectionRate(Math.max(0, s.getInfectionRate() - PACIFICATION_INFECTION));
			s.setContagionRate(Math.max(0, s.getContagionRate() - PACIFICATION_CONTAGION));
			s.setLethalityRate(Math.max(0, s.getLethalityRate() - PACIFICATION_LETHALITY));
		}
		
		if (GAME_MESSAGES) System.out.println(state.getOwner().getDisplayName() + " decreased global infection rate by " + PACIFICATION_INFECTION + ", global contation rate by " + PACIFICATION_CONTAGION + "% and global lethality rate by " + PACIFICATION_LETHALITY + "%");
	}
	
	private void executeWait(State state) {}
	
	private void mutation() {
		
		Random r = new Random();
		int mutationType = r.nextInt(3);
		
		for (State state : states) {
			
			if (mutationType == 0) {
				state.setInfectionRate(state.getInfectionRate() + MUTATION_INFECTION);				
			} else if (mutationType == 1) {
				state.setContagionRate(Math.min(100, state.getContagionRate() + MUTATION_CONTAGION));
			} else if (mutationType == 2) {
				state.setLethalityRate(Math.min(100, state.getLethalityRate() + MUTATION_LETHALITY));
			}
		}
				
		if (GAME_MESSAGES && mutationType == 0) System.out.println("Mutation : +" + MUTATION_INFECTION + " global infection rate");
		if (GAME_MESSAGES && mutationType == 1) System.out.println("Mutation : +" + MUTATION_CONTAGION + "% global contagion rate");
		if (GAME_MESSAGES && mutationType == 2) System.out.println("Mutation : +" + MUTATION_LETHALITY + "% global lethality rate");
	}
	
	private void reproduction() {
		
		if (round % BIRTH_ROUND == 0) {
			for (State state : states) {
				
				int healthyBirths = Math.floorDiv(state.getHealthy(), 2);
				int infectedBirths = Math.floorDiv(state.getInfected(), 2);
				
				state.setHealthy(state.getHealthy() + healthyBirths);
				state.setInfected(state.getInfected() + infectedBirths);
				
				if (GAME_MESSAGES && (healthyBirths + infectedBirths > 0)) {
					System.out.println(state.getOwner().getDisplayName() + " : " + (healthyBirths + infectedBirths) + " births (" +  healthyBirths + " healthy and " + infectedBirths + " infected)");
				}
			}
		}
	}
	
	private void migration() {
		
		int migrationHealthy = 0;
		int migrationInfected = 0;
		int migrationWeight = 0;
			
		// Emigration
		for (State state : states) {
			
			int stateMigrationHealthy = Math.min(state.getHealthy(), Math.floorDiv(state.getHealthy() * state.getMigrationRate(), 100));
			int stateMigrationInfected = Math.min(state.getHealthy(), Math.floorDiv(state.getInfected() * state.getMigrationRate(), 100));

			state.setHealthy(state.getHealthy() - stateMigrationHealthy);
			state.setInfected(state.getInfected() - stateMigrationInfected);

			migrationHealthy += stateMigrationHealthy;
			migrationInfected += stateMigrationInfected;
			migrationWeight += state.getMigrationRate();
			
			if (GAME_MESSAGES && (stateMigrationHealthy + stateMigrationInfected > 0)) System.out.println(state.getOwner().getDisplayName() + " : " + (stateMigrationHealthy + stateMigrationInfected) + " emigrated (" + stateMigrationHealthy + " healthy, " + stateMigrationInfected + " infected)");
		}
		
		int migrationHealtyRemainder = migrationHealthy;
		int migrationInfectedRemainder = migrationInfected;
		Map<State, Double> remainders = new LinkedHashMap<State, Double>();
		Map<State, Integer> migrantsHealthy = new HashMap<State, Integer>();
		Map<State, Integer> migrantsInfected = new HashMap<State, Integer>();

		// Immigration
		for (State state : states) {
			
			if (migrationWeight > 0) {
				
				int migrationRate = state.getMigrationRate();
				int migrationRatio = Math.floorDiv(migrationRate * 100, migrationWeight);
				int stateMigrationHealthy = Math.floorDiv(migrationHealthy * migrationRatio, 100);
				int stateMigrationInfected = Math.floorDiv(migrationInfected * migrationRatio, 100);
				
				double remainder = (double) (migrationHealthy * migrationRatio / 100.0) % 1.0;
				if (remainder <= 0.0)
					remainder = (double) (migrationInfected * migrationRatio / 100.0) % 1.0;
				if (remainder > 0.0)
					remainders.put(state, remainder);
				
				state.setHealthy(state.getHealthy() + stateMigrationHealthy);
				state.setInfected(state.getInfected() + stateMigrationInfected);
				
				migrationHealtyRemainder -= stateMigrationHealthy;
				migrationInfectedRemainder -= stateMigrationInfected;
								
				migrantsHealthy.put(state, stateMigrationHealthy);
				migrantsInfected.put(state, stateMigrationInfected);
			} else {
				migrantsHealthy.put(state, 0);
				migrantsInfected.put(state, 0);
			}
		}
		
		remainders = MapUtils.sortByValue(remainders);
		int index = 0;
		while (migrationHealtyRemainder + migrationInfectedRemainder > 0) {
			
			State state = (State) remainders.keySet().toArray()[index];
						
			if (migrationHealtyRemainder > 0) {
				state.setHealthy(state.getHealthy() + 1);
				migrantsHealthy.put(state, migrantsHealthy.get(state) + 1);
				migrationHealtyRemainder--;
			} else if (migrationInfectedRemainder > 0) {
				state.setInfected(state.getInfected() + 1);
				migrantsInfected.put(state, migrantsInfected.get(state) + 1);
				migrationInfectedRemainder--;
			}
		
			index++;
			
			if (index >= remainders.size()) 
				index = 0;
		}
		
		for (State state : states) {
			
			int stateMigrationHealthy = migrantsHealthy.get(state);
			int stateMigrationInfected = migrantsInfected.get(state);
			
			if (GAME_MESSAGES && (stateMigrationHealthy + stateMigrationInfected > 0)) System.out.println(state.getOwner().getDisplayName() + " : " + (stateMigrationHealthy + stateMigrationInfected) + " immigrated (" + stateMigrationHealthy + " healthy, " + stateMigrationInfected + " infected)");
		}
	}
	
	private void infection() {
		
		for (State state : states) {
			
			int infections = Math.min(state.getHealthy(), state.getInfectionRate());
			
			state.setHealthy(state.getHealthy() - infections);
			state.setInfected(state.getInfected() + infections);
			
			if (GAME_MESSAGES && (infections > 0)) {
				System.out.println(state.getOwner().getDisplayName() + " : " + infections + " infection(s)");
			}
		}
	}
	
	private void contagion() {
		
		for (State state : states) {
			
			int contagions = Math.min(state.getHealthy(), Math.floorDiv(state.getInfected() * state.getContagionRate(), 100));
			
			state.setHealthy(state.getHealthy() - contagions);
			state.setInfected(state.getInfected() + contagions);
			
			if (GAME_MESSAGES && (contagions > 0)) {
				System.out.println(state.getOwner().getDisplayName() + " : " + contagions + " contagion(s)");
			}
		}
	}
	
	private void extinction() {
		
		for (State state : states) {
			
			int deaths = Math.min(state.getInfected(), Math.floorDiv(state.getInfected() * state.getLethalityRate(), 100));
			
			state.setInfected(state.getInfected() - deaths);
			state.setDead(state.getDead() + deaths);
			
			if (GAME_MESSAGES && (deaths > 0)) {
				System.out.println(state.getOwner().getDisplayName() + " : " + deaths + " death(s)");
			}
		}
	}
	
	private String generateArgs() {
		
		StringBuilder builder = new StringBuilder();
		//PlayerID_Healthy_Infected_Dead_IR_CR_LR_MR
		for (State state : states) {
			builder.append(';');
			builder.append(state.getOwner().getId()).append('_');
			builder.append(state.getHealthy()).append('_');
			builder.append(state.getInfected()).append('_');
			builder.append(state.getDead()).append('_');
			builder.append(state.getInfectionRate()).append('_');
			builder.append(state.getContagionRate()).append('_');
			builder.append(state.getLethalityRate()).append('_');
			builder.append(state.getMigrationRate());
		}
		return builder.toString();
	}
	
	private List<Score> printResults() {
		
		List<Score> scores = new ArrayList<Score>();
		
		System.out.println("********** FINISH **********");
		
		for (Player player : players) {
			int healthy = 0;
			int infected = 0;
			int dead = 0;
			
			for (State state : states) {
				if (player.equals(state.getOwner())) {
					healthy += state.getHealthy();
					infected += state.getInfected();
					dead += state.getDead();
				}
			}
			
			scores.add(new Score(player, healthy, infected, dead));
		}
		
		//sort descending
		Collections.sort(scores, Collections.reverseOrder());
		
		for (int i = 0; i < scores.size(); i++) {
			Score score = scores.get(i);
			System.out.println(i+1 + ". " + score.print());
		}
		
		return scores;
		
	}
	
	private void printStatus() {
		
		List<Status> status = new ArrayList<Status>();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		for (Player player : players) {
			int healthy = 0;
			int infected = 0;
			int dead = 0;
			int infectionRate = 0;
			int contagionRate= 0;
			int lethalityRate = 0;
			int migrationRate = 0;
			
			for (State state : states) {
				if (player.equals(state.getOwner())) {
					healthy += state.getHealthy();
					infected += state.getInfected();
					dead += state.getDead();
					infectionRate += state.getInfectionRate();
					contagionRate += state.getContagionRate();
					lethalityRate += state.getLethalityRate();
					migrationRate += state.getMigrationRate();
				}
			}
			
			status.add(new Status(player, healthy, infected, dead, infectionRate, contagionRate, lethalityRate, migrationRate));
		}
		
		//sort descending
		Collections.sort(status, Collections.reverseOrder());
		
		for (int i = 0; i < status.size(); i++) {
			Status stat = status.get(i);
			System.out.println(i+1 + ". " + stat.print());
		}
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
	}
	
}

