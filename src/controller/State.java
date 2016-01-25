package controller;
import java.util.Scanner;

public class State {
	private final int id;
	private Player owner;
	
	private Integer sane;
	private Integer infected;
	private Integer dead;
	private Integer infectionRate;
	private Integer contagionRate;
	private Integer lethalityRate;
	private Integer migrationRate;	

	public State(int id, Player owner) {
		super();
		this.id = id;
		this.owner = owner;
	}
	
	public State(int id, Player owner, Integer sane, Integer infected,
			Integer dead, Integer infectionRate, Integer contagionRate,
			Integer lethalityRate, Integer migrationRate) {
		super();
		this.id = id;
		this.owner = owner;
		this.sane = sane;
		this.infected = infected;
		this.dead = dead;
		this.infectionRate = infectionRate;
		this.contagionRate = contagionRate;
		this.lethalityRate = lethalityRate;
		this.migrationRate = migrationRate;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}
	
	public boolean isAlive() {
		return (getSane() + getInfected()) > 0 ;
	}

	public String getCommand(String args) throws Exception {
		//neutral player
		if ("".equals(owner.getCmd())) {
			return "NNN";
		}
		Process proc = null;
		Scanner stdin = null;
		try {
			proc = Runtime.getRuntime().exec(owner.getCmd() + " " + args);
			stdin = new Scanner(proc.getInputStream());
			StringBuilder response = new StringBuilder();
			while (stdin.hasNext()) {
				response.append(stdin.next()).append(' ');
			}
			return response.toString();	
		} finally {
			if (stdin != null) stdin.close();
			if (proc != null) proc.destroy();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other != null && other instanceof State) {
			return getId() == ((State) other).getId();
		}
		return false;
	}
	
	@Override
	public String toString() {
		//return "Id: " + id + " Owner: " + owner.getDisplayName();
		return owner.getDisplayName();
	}

	public Integer getSane() {
		return sane;
	}

	public void setSane(Integer sane) {
		this.sane = sane;
	}

	public Integer getInfected() {
		return infected;
	}

	public void setInfected(Integer infected) {
		this.infected = infected;
	}

	public Integer getDead() {
		return dead;
	}

	public void setDead(Integer dead) {
		this.dead = dead;
	}

	public Integer getInfectionRate() {
		return infectionRate;
	}

	public void setInfectionRate(Integer infectionRate) {
		this.infectionRate = infectionRate;
	}

	public Integer getContagionRate() {
		return contagionRate;
	}

	public void setContagionRate(Integer contagionRate) {
		this.contagionRate = contagionRate;
	}

	public Integer getLethalityRate() {
		return lethalityRate;
	}

	public void setLethalityRate(Integer lethalityRate) {
		this.lethalityRate = lethalityRate;
	}

	public Integer getMigrationRate() {
		return migrationRate;
	}

	public void setMigrationRate(Integer migrationRate) {
		this.migrationRate = migrationRate;
	}
	
	
	
}
