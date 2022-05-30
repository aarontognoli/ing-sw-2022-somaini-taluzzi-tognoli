package it.polimi.ingsw.cards.characters.HerbalistCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.places.Island;

public class HerbalistCharacter extends CharacterCard {
    int entryTilesInIslandCount;

    public HerbalistCharacter(Model model) {
        super(model, 2);
        entryTilesInIslandCount = 0;
    }

    public void moveEntryTileBackToCard() {
        if (entryTilesInIslandCount == 0) {
            throw new RuntimeException(
                    "Moving a no-entry tile back to HerbalistCharacter while there are no no-entry tiles around. What?"
            );
        }
        entryTilesInIslandCount--;
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        throw new RuntimeException("Not implemented yet :)");
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Integer islandIndex)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        if (entryTilesInIslandCount == 4) {
            throw new CCArgumentException("There are no more No Entry tiles to use");
        }

        Island targetIsland;
        try {
            targetIsland = model.publicModel.getIslands().get(islandIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new CCArgumentException("Invalid Island number");
        }

        try {
            targetIsland.putNoEntryTile();
            entryTilesInIslandCount++;
        } catch (Exception e) {
            throw new CCArgumentException(e.getMessage());
        }
    }
}
