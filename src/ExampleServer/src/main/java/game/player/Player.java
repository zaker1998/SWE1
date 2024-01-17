package game.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class Player extends SUniquePlayerIdentifier implements IPlayerAccesser {

	private final PlayerInformation playerInf;

	private boolean collectedTreasure = false;

	private static Logger logger = LoggerFactory.getLogger(Player.class);

	public Player(String playerID, PlayerInformation playerInf) {
		super(playerID);
		this.playerInf = playerInf;
	}

	public Player(String playerID, String firstName, String lastName, String studentID) {
		this(playerID, new PlayerInformation(firstName, lastName, studentID));
	}

	public Player(SUniquePlayerIdentifier playerID, PlayerInformation playerInf) {
		this(playerID.asString(), playerInf);
	}

	public static Player getRandomPlayer(PlayerInformation playerInf) {
		SUniquePlayerIdentifier randomID = SUniquePlayerIdentifier.getRandomID();
		return new Player(randomID, playerInf);
	}

	public static Player getRandomPlayer(String firstName, String lastName, String studentID) {
		return getRandomPlayer(new PlayerInformation(firstName, lastName, studentID));
	}

	public void collectTreasure() {
		if (collectedTreasure) {
			logger.error("collected treasure even though the treassure has already been collected."
					+ "This should not be possible. Continueing execution and hoping it wont cause any problems!");
		}
		collectedTreasure = true;
	}

	@Override
	public SUniquePlayerIdentifier getPlayerID() {
		return this;
	}

	@Override
	public String getFirstName() {
		return playerInf.getFirtName();
	}

	@Override
	public String getLastName() {
		return playerInf.getLastName();
	}

	@Override
	public String getStudentID() {
		return playerInf.getStudentID();
	}

	@Override
	public boolean getCollectedTreasure() {
		return collectedTreasure;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof SUniquePlayerIdentifier)) {
			return false;
		}

		SUniquePlayerIdentifier other = (SUniquePlayerIdentifier) obj;

		if (!other.asString().equals(getPlayerID().asString()))
			return false;
		return true;
	}

}
