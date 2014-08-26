/*
 * The MIT License
 *
 * Copyright 2014 Drareeg.
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
package Domain.Chess.Variants;
import Shared.Chess.Board;
import Domain.Chess.PositionJuror;
import Domain.Chess.TurnSystem;
import Domain.Chess.ClassicTurnSystem;
import Shared.Chess.Coordinate;
import Shared.Networking.Message;

/**
 *
 * @author Drareeg
 */
public class NormalChess {
    protected Board board;
    protected PositionJuror positionJuror;

    public NormalChess() {
        positionJuror = new PositionJuror();
        board = new Board();
    }

    public void movePiece(Coordinate fromCoord, Coordinate toCoord) {
        //overlaten aan het stuk, wegens promotie en rokade
        board.getPiece(fromCoord).executeMove(fromCoord, toCoord, board);
        //varianten overschrijven postMove
        postMove(fromCoord, toCoord);
    }

    //opgepast, als andere klassen een reference hebben naar het board, dan poept deze methode die klassen
    //beter toch testen op de copy ipv op dit bord. dat veranderen zou minder lang geduurd hebben dan deze comment
    //tja
    public boolean isMoveAllowed(Coordinate fromCoord, Coordinate toCoord) {
        if (!board.isValidCoordinate(fromCoord) || !board.isValidCoordinate(toCoord) || toCoord.equals(fromCoord) || !board.hasPiece(fromCoord)) {
            System.out.println("move not allowed bacause invalid coord  " + fromCoord + " " + toCoord);
            return false;
        }
        //TODO: kijken of dat stuk wel aan zet is ook, en of de message komt van die speler (mss dat hogerop controleren)
        if (!board.getPiece(fromCoord).canReachFromTo(fromCoord, toCoord, board)) {
            return false;
        }
        //maak een backup van de huidige stand van zaken, voer de zet uit, controleer of we nu een geldige state hebben, doe een recover naar de backup
        boolean isAllowed;
        Board boardBackup = this.board;
        Board boardCopy = this.board.getDeepCopy();
        this.board = boardCopy;
        boolean playerIsWhite = board.getPiece(fromCoord).isWhite();
        movePiece(fromCoord, toCoord);
        isAllowed = positionJuror.isValid(board, playerIsWhite);
        board = boardBackup;
        return isAllowed;
    }

    public boolean isLost(boolean checkingForWhite) {
        return positionJuror.isLost(this, board, checkingForWhite);
    }

    public boolean isStaleMated(boolean checkingForWhite) {
        return positionJuror.isStaleMated(this, board, checkingForWhite);
    }

    //in normale schaak is er geen actie na een zet
    public void postMove(Coordinate fromCoord, Coordinate toCoord) {
    }

    //sommige schaakvarianten moeten kunnen reageren op messages, bv verborgen dame op de keuze van de dame.
    public void handleCustomMessage(Message message) {
    }

    public boolean isValidCoordinate(Coordinate testCoord) {
        return board.isValidCoordinate(testCoord);
    }

    //in principe kan er nog een abstracte klasse boven normalchess met de methoden isvalidcoordinate, isstalmated, getpiece ....
    public Board getBoard() {
        return board;
    }

    public TurnSystem getTurnSystem() {
        return new ClassicTurnSystem();
    }
}
