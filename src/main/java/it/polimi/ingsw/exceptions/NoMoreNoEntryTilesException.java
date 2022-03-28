package it.polimi.ingsw.exceptions;

public class NoMoreNoEntryTilesException extends Exception{
    public NoMoreNoEntryTilesException(){
        super("There are no more No Entry tiles to use");
    }
}
