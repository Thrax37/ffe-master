package players;

import controller.Player;

public class PureBot extends Player {

	@Override
	public String getCmd() {
		return "C:\\Haskell-7.10.3\\bin\\runhaskell.exe PureBot.hs";
	}	
}
