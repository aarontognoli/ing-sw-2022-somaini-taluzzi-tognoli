package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.notifier.Notifier;

/**
 * Base Class for the View, sends a message notification to its listener (a controller)
 */
public abstract class View extends Notifier<ClientMessage> {

}
