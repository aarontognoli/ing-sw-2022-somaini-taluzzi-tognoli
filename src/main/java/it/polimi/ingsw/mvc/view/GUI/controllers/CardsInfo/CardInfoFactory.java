package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.enums.CharacterCardsEffectArguments;

public class CardInfoFactory {
    public static CardInfoController createController(CharacterCardsEffectArguments argumentType) {
        switch (argumentType) {

            case BARD -> {
                return new CardInfoBard();
            }
            case JOKER -> {
                return new CardInfoJoker();
            }
            case WINE -> {
                return new CardInfoWine();
            }
            case COLOR -> {
                return new CardInfoColor();
            }
            case ISLAND -> {
                return new CardInfoIsland();
            }
        }
        return new CardInfoNone();
    }
}
