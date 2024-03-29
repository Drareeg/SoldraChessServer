/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme & Geerard Ponnet.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Shared.Networking;

/**
 *
 * @author Dries
 */
public interface MessageHandler {

    public void handleJoinLobby(JoinLobbyMessage message);

    public void handleChallenge(ChallengeMessage challengeMessage);

    public void handleMove(MoveMessage moveMessage);

    public void handleGameStart(GameStartMessage gameStart);

    public void handleThisIsTheLobbyMessage(ThisIsTheLobbyMessage thisIsTheLobby);

    public void handleLeaveLobby(LeaveLobbyMessage leaveLobby);

    public void handleChatMessage(ChatMessage aThis);

    public void handleThisIsTheBoard(ThisIsTheBoardMessage aThis);

    public void handleTurnMessage(TurnMessage aThis);

    public void handleAcceptChallenge(AcceptChallengeMessage aThis);

    public void handleGameFinished(GameFinishedMessage aThis);

    public void handleSurrender(SurrenderMessage aThis);

    public void handleThisIsMyHiddenQueenMessage(ThisIsMyHiddenQueenMessage aThis);
}
