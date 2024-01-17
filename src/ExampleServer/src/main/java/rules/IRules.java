package rules;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;

public interface IRules {
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration);

	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap);

	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID);

	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove);

}
