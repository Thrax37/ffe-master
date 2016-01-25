package controller;


import java.util.Scanner;

public abstract class Player {
	private int id;
	private int score;

	public final void setId(int id) {
		this.id = id;
	}

	public abstract String getCmd();
	
	public final int getId() {
		return id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public final String prepare() throws Exception {
		String response = null;
		Process proc = null;
		Scanner stdin = null;
		try {
			proc = Runtime.getRuntime().exec(getCmd());
			stdin = new Scanner(proc.getInputStream());
			response = stdin.next();
			
			return response;
		} finally {
			if (stdin != null)
				stdin.close();
			if (proc != null)
				proc.destroy();
		}
	}
	
	public final String getDisplayName() {
		return getClass().getName().replace("players.", "");
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
		if (other != null && other instanceof Player) {
			return getId() == ((Player) other).getId();
		}
		return false;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
	
	
}
