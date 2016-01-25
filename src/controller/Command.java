package controller;


public class Command {
	private final State source;
	private String command;
	private String[] args;
	
	public Command(String string, State source) throws Exception {
		this.source = source;
		
		String[] splitted = string.split(" ");
		if (splitted.length < 1) {
			throw new IllegalArgumentException();
		}
		
		this.command = splitted[0]; 
		this.args = new String[splitted.length - 1]; 
		
		for (int i = 0; i < this.args.length; i++)
		{
			this.args[i] = splitted[i + 1];
		}
		
	}

	public State getSource() {
		return source;
	}

	public String getCommand() {
		return command;
	}

	public String[] getArgs() {
		return args;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		
		return true;
	}	
}
