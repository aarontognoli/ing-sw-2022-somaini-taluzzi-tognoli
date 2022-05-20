package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.notifier.Subscriber;

/**
 * The controller, which subscribes for messages from the view, and acts accordingly
 */
public abstract class Controller implements Subscriber<ClientMessage> {
}