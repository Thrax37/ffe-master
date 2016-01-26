package players;

import controller.Player;

public class AllOrNothing extends Player {

	@Override
	public String getCmd() {
		return "C:\\R-3.2.3\\bin\\Rscript.exe AllOrNothing.R";
	}	
}
