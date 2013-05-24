package battleships.message;

public enum MessageType
{
	/**
	 * Should be the first message sent to the Server to establish
	 * that the connection is sound, and in order to give the player a name.
	 * <br/>
	 * <br/>- Text (Desired name)
	 */
	CONNECT,
	
	/**
	 * The client may request to get an updated version of all players. This is
	 * necessary when the lobby listing is to be built or refreshed.
	 */
	REQUEST,
	
	/**
	 * One update message contains information about one specific player. Note that these
	 * are only sent when the client has asked for it with a REQUEST message.
	 */
	UPDATE,
	
	/**
	 * Is sent to the server when the player desires to challenge another.
	 * The server notifies the challenged user with the same message, but
	 * with the challenger's ID instead.
	 * <br/>
	 * <br/>- Integer (ID of the other player)
	 */
	CHALLENGE,
	
	/**
	 * Yes, sir! I accept the challenge!
	 * <br/>
	 * <br/>- Integer (ID of the other player)
	 */
	ACCEPT,
	
	/**
	 * No... I don't want to battle you.
	 * <br/>
	 * <br/>- Integer (ID of the other player)
	 */
	DENY,
	
	/**
	 * Is sent to the client when a game session has begun for the player.
	 * This means that the placement of ships can commence.
	 */
	GAME,
	
	/**
	 * Tells the server where the player wants a specific ship.
	 * <br/>
	 * <br/>- Which ship?
	 * <br/>- Rotation?
	 * <br/>- Coordinate (Where ship is located on the map)
	 */
	PLACE,
	
	/**
	 * Both players must send this message to declare that they are
	 * satisfied with how they've placed their ships. The server
	 * responds with a TURN message.
	 */
	READY,
	
	/**
	 * Defines a change of turn and tells which player should do a move next
	 * <br/>
	 * <br/>- Integer (ID of the player who gets the turn)
	 */
	TURN,
	
	/**
	 * Hey, server! I'm firing mah stuff!
	 * <br/>
	 * <br/>- Coordinate (Target)
	 */
	FIRE,
	
	/**
	 * Something was hit!
	 * <br/>
	 * <br/>- Coordinate (Target)
	 */
	HIT,
	
	/**
	 * A shot into the ocean.
	 * <br/>
	 * <br/>- Coordinate (Target)
	 */
	MISS,
	
	/**
	 * You won! At this point the game session is terminated.
	 */
	WIN,
	
	/**
	 * You lost! At this point the game session is terminated.
	 */	
	LOSE
}