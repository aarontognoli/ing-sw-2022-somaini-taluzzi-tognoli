package it.polimi.ingsw.cards.characters.PostManCharacter;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.player.Player;

public class PostManCharacter extends CharacterCard {

    public PostManCharacter(Model model) { super(model, 1); }

    @Override
    public void internalActivateEffect(Object arguments) {
        Player player = model.publicModel.getCurrentPlayer();
        player.setMaxMotherNatureMovementValue(player.getMaxMotherNatureMovementValue() + 2);
    }
}
