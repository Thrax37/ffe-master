package controller;


public class Status implements Comparable<Status> {
	private Player player;
	private int healthy;
	private int infected;
	private int dead;
	private int infectionRate;
	private int contagionRate;
	private int lethalityRate;
	private int migrationRate;
	
	public Status(Player player, int healthy, int infected, int dead, int infectionRate, int contagionRate, int lethalityRate, int migrationRate) {
		super();
		this.player = player;
		this.healthy = healthy;
		this.infected = infected;
		this.dead = dead;
		this.infectionRate = infectionRate;
		this.contagionRate = contagionRate;
		this.lethalityRate = lethalityRate;
		this.migrationRate = migrationRate;
	}
	
	public String print() {
		return player.getDisplayName() + "[" + healthy + ", " + infected + ", " + dead 
				+ ", " + infectionRate + ", " + contagionRate + ", " + lethalityRate
				+ ", " + migrationRate + "]";
	}
	
	@Override
	public int compareTo(Status other) {
		if (healthy > other.healthy) {
			return 1;
		} else if (healthy < other.healthy){
			return -1;
		}
		
		if (infected > other.infected) {
			return 1;
		} else if (infected < other.infected){
			return -1;
		}
		
		if (dead < other.dead) {
			return 1;
		} else if (dead > other.dead){
			return -1;
		}
		
		return 0;
	}	
}
