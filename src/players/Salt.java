package players;

import controller.Player;

public class Salt extends Player {

	@Override
	public String getCmd() {
		return "C:\\kotlinc\\bin\\kotlin.bat salt.SaltKt";
	}	
}
