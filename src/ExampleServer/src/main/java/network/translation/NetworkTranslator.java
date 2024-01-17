package network.translation;

import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;
import exceptions.InternalServerException;
import game.SGameState;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.move.helpers.ESMove;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class NetworkTranslator {

	GameStateExtractor gameStateTranslate = new GameStateExtractor();
	NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();

	public SUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new SUniqueGameIdentifier(gameID.getUniqueGameID());
	}

	public UniqueGameIdentifier internalGameIDToNetwork(SUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.asString());
	}

	public SUniquePlayerIdentifier networkPlayerIDToInternal(UniquePlayerIdentifier playerID) {
		return new SUniquePlayerIdentifier(playerID.getUniquePlayerID());
	}

	public UniquePlayerIdentifier internalPlayerIDToNetwork(SUniquePlayerIdentifier playerID) {
		return new UniquePlayerIdentifier(playerID.asString());
	}

	public PlayerInformation networkPlayerRegistrationtoInternal(PlayerRegistration playerReg) {
		return new PlayerInformation(playerReg.getStudentFirstName(), playerReg.getStudentLastName(),
				playerReg.getStudentUAccount());
	}

	public SHalfMap networkHalfMapToInernal(PlayerHalfMap halfmap) {
		return halfMapTrans.translateNetworkHalfMap(halfmap);
	}

	public GameState internalGameStateToNetwork(SGameState gameState) {
		return gameStateTranslate.extractGameState(gameState);
	}

	public ESMove networkMoveToInternal(EMove move) {
		switch (move) {
		case Down:
			return ESMove.DOWN;
		case Left:
			return ESMove.LEFT;
		case Right:
			return ESMove.RIGHT;
		case Up:
			return ESMove.UP;
		}
		throw new InternalServerException("Failed to convert network move to internal.");
	}

}
