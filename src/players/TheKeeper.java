package players;

import controller.Player;

public class TheKeeper extends Player {

	@Override
	public String getCmd() {
		return "C:\\lua-5.2.3\\lua52.exe TheKeeper.lua";
	}	
}
