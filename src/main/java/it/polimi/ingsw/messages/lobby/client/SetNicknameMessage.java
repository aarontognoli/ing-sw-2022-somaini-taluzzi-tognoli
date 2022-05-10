package it.polimi.ingsw.messages.lobby.client;

public class SetNicknameMessage extends ClientLobbyMessage {
    final private String nickname;

    public SetNicknameMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
