package controller;


public class Score implements Comparable<Score> {
	private Player player;
	private int healthy;
	private int infected;
	private int dead;
	
	public Score(Player player, int healthy, int infected, int dead) {
		super();
		this.player = player;
		this.healthy = healthy;
		this.infected = infected;
		this.dead = dead;
	}
	
	public String print() {
		return player.getDisplayName() + " (" + healthy + ", " + infected + ", " + dead + ")";
	}
	
	@Override
	public int compareTo(Score other) {
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
