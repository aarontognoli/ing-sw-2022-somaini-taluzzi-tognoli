package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.pawn.*;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

import java.util.*;

public class Model {

    static final int TOTAL_ISLANDS_NUMBER = 12;
    List<Island> islands;

    // Professor with board that has them
    List<Professor> professors;

    // Player data and their board
    int totalPlayerCount;
    List<Player> players;
    Player currentPlayer;

    GameMode gameMode;

    // Bag where I can draw new students
    Bag bag;

    // Clouds in the middle
    List<Cloud> clouds;

    MotherNature motherNature;

    // Character cards
    List<CharacterCard> currentGameCards;

    // Strategy field for Influence Calculation
    InfluenceCalculator influenceCalculator;

    // Models
    final PrivateModel privateModel;
    public final PublicModel publicModel;
    public final CharacterModel characterModel;

    // Initialize game with starting rules

    public Model(int motherNatureStartingPosition, Map<String, DeckName> nicknamesAndDecks, GameMode gameMode)
            throws IllegalArgumentException, Exception {

        // Models
        privateModel = new PrivateModel(this);
        publicModel = new PublicModel(this);
        characterModel = new CharacterModel(this);

        professors = new ArrayList<>();
        players = new ArrayList<>();
        clouds = new ArrayList<>();

        this.gameMode = gameMode;

        // Initialize islands
        islands = new ArrayList<Island>();
        for (int i = 0; i < TOTAL_ISLANDS_NUMBER; i++) {
            islands.add(new Island());
        }
        // Initialize Professors
        for (Color c : Color.values()) {
            professors.add(c.ordinal(), new Professor(c));
        }

        // TODO: different influence rules for each number of player with case select
        int towerColor = 0;

        privateModel.placeMotherNature(motherNatureStartingPosition);

        totalPlayerCount = nicknamesAndDecks.size();
        if (nicknamesAndDecks.size() > 1 && nicknamesAndDecks.size() <= 4) {

            // 2 players
            for (String nickname : nicknamesAndDecks.keySet()) {
                players.add(new Player(nickname, TowerColor.values()[towerColor], nicknamesAndDecks.get(nickname), 8));
                towerColor++;
            }
        } else
            throw new IllegalArgumentException("Illegal number of Players");

        // Initialize InfluenceCalculator
        influenceCalculator = totalPlayerCount == 4 ? new InfluenceCalculator_4(this)
                : new InfluenceCalculator_2_3(this);

        // Initialize clouds
        if (totalPlayerCount == 2 || totalPlayerCount == 4) {
            for (int i = 0; i < totalPlayerCount; i++) {
                clouds.add(new Cloud(3));
            }
        } else if (totalPlayerCount == 3) {
            for (int i = 0; i < 3; i++) {
                clouds.add(new Cloud(4));
            }
        }
        privateModel.prepareMatch(motherNatureStartingPosition);
    }

}
