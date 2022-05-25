package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

import java.util.List;

public abstract class ClientView extends View implements Subscriber<ServerMessage> {
    protected Model model;
    protected String myUsername;

    public ClientView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        messageNotifier.addSubscriber(this);
        new ModelSubscriber(modelNotifier);
    }

    public abstract void show();

    protected abstract void showModel();

    public abstract void run() throws InterruptedException;

    /**
     * Notification sent by the model every time it updates
     *
     * @param newMessage newValue of the model
     */
    @Override
    public abstract void subscribeNotification(ServerMessage newMessage);

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getIslandCountFromModel() {
        return model.publicModel.getIslandCount();
    }

    public GameMode getGameMode() {
        return model.publicModel.getGameMode();
    }

    public int getCurrentPlayerMaxMotherNatureMovement() {
        return model.publicModel.getCurrentPlayer().getMaxMotherNatureMovementValue();
    }

    public int getCloudsCount() {
        return model.publicModel.getCloudsCount();
    }

    public List<CharacterCard> getCurrentGameCards() {
        return model.publicModel.getCurrentGameCards();
    }

    class ModelSubscriber implements Subscriber<Model> {

        ModelSubscriber(Notifier<Model> modelNotifier) {
            modelNotifier.addSubscriber(this);
        }

        /**
         * Notification sent by the model every time it updates
         *
         * @param newValue newValue of the model
         */
        @Override
        public void subscribeNotification(Model newValue) {
            model = newValue;
            show();
        }
    }
}
