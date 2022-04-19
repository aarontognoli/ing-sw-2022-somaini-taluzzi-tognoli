package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.pawn.MotherNature;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {

    private static final int INITIAL_TOWER_COUNT_2_4_PLAYERS = 8;
    private static final int INITIAL_TOWER_COUNT_3_PLAYERS = 6;

    public static final int TOTAL_ISLANDS_NUMBER = 12;
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

    // Strategy field for Professor Move Rule
    ProfessorMoverRuleDefault professorMoverRule;

    // Models
    final PrivateModel privateModel;
    public final PublicModel publicModel;
    public final CharacterModel characterModel;

    // Initialize game with starting rules

    public Model(int motherNatureStartingPosition, Map<String, DeckName> nicknamesAndDecks, GameMode gameMode)
            throws Exception {

        // Models
        privateModel = new PrivateModel(this);
        publicModel = new PublicModel(this);
        characterModel = new CharacterModel(this);

        professors = new ArrayList<>();
        players = new ArrayList<>();
        clouds = new ArrayList<>();

        this.gameMode = gameMode;

        // Initialize islands
        islands = new ArrayList<>();
        for (int i = 0; i < TOTAL_ISLANDS_NUMBER; i++) {
            islands.add(new Island());
        }
        // Initialize Professors
        for (Color c : Color.values()) {
            professors.add(c.ordinal(), new Professor(c));
        }

        privateModel.placeMotherNature(motherNatureStartingPosition);

        int towerColorIndex = 0;
        totalPlayerCount = nicknamesAndDecks.size();
        switch (totalPlayerCount) {
            case 2:
                for (String nickname : nicknamesAndDecks.keySet()) {
                    players.add(
                            new Player(nickname, TowerColor.values()[towerColorIndex], nicknamesAndDecks.get(nickname),
                                    INITIAL_TOWER_COUNT_2_4_PLAYERS));
                    towerColorIndex++;
                }
                break;
            case 3:
                for (String nickname : nicknamesAndDecks.keySet()) {
                    players.add(
                            new Player(nickname, TowerColor.values()[towerColorIndex], nicknamesAndDecks.get(nickname),
                                    INITIAL_TOWER_COUNT_3_PLAYERS));
                    towerColorIndex++;
                }
                break;

            case 4:
                for (String nickname : nicknamesAndDecks.keySet()) {

                    TowerColor towerColor = towerColorIndex < 2 ? TowerColor.WHITE : TowerColor.BLACK;
                    int towerCount = towerColorIndex % 2 == 0 ? INITIAL_TOWER_COUNT_2_4_PLAYERS : 0;
                    players.add(new Player(
                            nickname, towerColor, nicknamesAndDecks.get(nickname), towerCount));
                    towerColorIndex++;
                }
                break;

            default:
                throw new IllegalArgumentException("Illegal number of Players");
        }

        // Initialize InfluenceCalculator
        influenceCalculator = totalPlayerCount == 4 ? new InfluenceCalculator_4(this)
                : new InfluenceCalculator_2_3(this);

        professorMoverRule = new ProfessorMoverRuleDefault();

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

        currentPlayer = players.get(0);

        privateModel.prepareMatch(motherNatureStartingPosition);
    }

}
