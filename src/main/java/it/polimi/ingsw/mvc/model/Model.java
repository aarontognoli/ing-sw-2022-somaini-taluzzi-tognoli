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
    public Model() {
        // Models
        privateModel = new PrivateModel(this);
        publicModel = new PublicModel(this);
        characterModel = new CharacterModel(this);

        professors = new ArrayList<>();
        players = new ArrayList<>();
        clouds = new ArrayList<>();

        // Initialize islands
        islands = new ArrayList<Island>();
        for (int i = 0; i < TOTAL_ISLANDS_NUMBER; i++) {
            islands.add(new Island());
        }
    }

}
