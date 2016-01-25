package controller;


public class Score implements Comparable<Score> {
	private Player player;
	private int sane;
	private int infected;
	private int dead;
	
	public Score(Player player, int sane, int infected, int dead) {
		super();
		this.player = player;
		this.sane = sane;
		this.infected = infected;
		this.dead = dead;
	}
	
	public String print() {
		return player.getDisplayName() + " (" + sane + ", " + infected + ", " + dead + ")";
	}
	
	@Override
	public int compareTo(Score other) {
		if (sane > other.sane) {
			return 1;
		} else if (sane < other.sane){
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
