package reversi;

import java.util.ArrayList;
import java.util.List;

public abstract class GameModel {
	public enum BoardColor{None, Black, White};
	public interface IndexUpdate {
		int update(int i);
	}
	public static IndexUpdate increment = (i)->{return i + 1;};
	public static IndexUpdate decrement = (i)->{return i - 1;};
	public static IndexUpdate identity = (i)->{return i;};
	
	private List <List<BoardColor>> checkerboard;
	private int halfSize;
	private int count;
	private BoardColor currPlayer;
	private BoardColor winner;
	
	abstract public void movePiece(int i, int j, BoardColor color);
	abstract public void updateCurrentPlayer();
	abstract public void gameStop();
	
	public GameModel(int halfSize) {
		this.halfSize = halfSize;
		this.checkerboard = new ArrayList<>();
		for (int i = 0; i < 2 * this.halfSize; i++) {
			List<BoardColor> newList = new ArrayList<>(2 * this.halfSize);
			for (int j = 0; j < 2 * this.halfSize; j++) {
				newList.add(BoardColor.None);
			}
			checkerboard.add(newList);
		}
		this.checkerboard.get(halfSize-1).set(halfSize-1, BoardColor.Black);
		this.checkerboard.get(halfSize-1).set(halfSize, BoardColor.White);
		this.checkerboard.get(halfSize).set(halfSize-1, BoardColor.White);
		this.checkerboard.get(halfSize).set(halfSize, BoardColor.Black);
		this.currPlayer = BoardColor.Black;
		this.winner = BoardColor.None;
		this.count = 4 * this.halfSize * this.halfSize;
		this.count -= 4;
	}
	private BoardColor boardGet(int i, int j) {
		if (i < 0 ||
			i >= 2 * halfSize ||
			j < 0 ||
			j >= 2 * halfSize) {
			return BoardColor.None;
		}
		return this.checkerboard.get(i).get(j);
	}
	
	private void boardSet(int i, int j, BoardColor color) {
		if (i < 0 ||
			i >= 2 * halfSize ||
			j < 0 ||
			j >= 2 * halfSize) {
			return;
		}
		this.checkerboard.get(i).set(j, color);
		movePiece(i, j, color);
		return;
	}
	private void checkWinner() {
		if (this.winner == BoardColor.None) {
			int white = 0;
			int black = 0;
			for (int i = 0; i < 2 * this.halfSize; i++) {
				for (int j = 0; j < 2 * this.halfSize; j++) {
					if (boardGet(i, j) == BoardColor.Black)
						black++;
					if (boardGet(i, j) == BoardColor.White)
						white++;
				}
			}
			if (white == 0) {
				this.winner = BoardColor.Black;
				return;
			}
			if (black == 0) {
				this.winner = BoardColor.White;
				return;
			}
			if(count == 0) {
				if (white > black) {
					this.winner = BoardColor.White;
				} else if (black > white) {
					this.winner = BoardColor.Black;
				} else {
					this.winner = BoardColor.None;
				}
			}
		}
		
	}
	private boolean recFlip(BoardColor startColor, int i, int j, IndexUpdate iUpdate, IndexUpdate jUpdate, boolean doFlip) {
		if (boardGet(i, j) == BoardColor.None) {
			return false;
		} else if (boardGet(i, j) != startColor) {
			boolean result = recFlip(startColor, iUpdate.update(i), jUpdate.update(j), iUpdate, jUpdate, doFlip);
			if (result) {
				if (doFlip) {
					boardSet(i, j, startColor);
				}
			}
			return result;
		} else {
			return true;
		}
	}
	private boolean flip(BoardColor startColor, int i, int j, IndexUpdate iUpdate, IndexUpdate jUpdate, boolean doFlip) {
		if (startColor == boardGet(iUpdate.update(i), jUpdate.update(j)))
			return false;
		return recFlip(startColor, iUpdate.update(i), jUpdate.update(j), iUpdate, jUpdate, doFlip);
	}
	
	public boolean moves(int i, int j) {
		if (i < 0 ||
			i >= 2 * halfSize ||
			j < 0 ||
			j >= 2 * halfSize) {
			return false;
		}
		// Check current location
		if (boardGet(i, j) != BoardColor.None) {
			return false;
		}
		
		boolean validLoc = false;
		// Change color
		// Up 
		validLoc = checkLocValid(i, j, currPlayer, true);
		
		if (validLoc) {
			boardSet(i, j, currPlayer);
			this.count--;
			this.checkWinner();
			this.currPlayer = this.getNextPlayer();
			updateCurrentPlayer();
			if (this.getWinner() != BoardColor.None || this.count == 0) {
				gameStop();
			}
			return true;
		} else {
			return false;
		}
	}

	private BoardColor getNextPlayer() {
		BoardColor nextPlayer = currPlayer == BoardColor.Black? BoardColor.White: BoardColor.Black;
		for (int i = 0; i < 2 * this.halfSize; i++) {
			for (int j = 0; j < 2 * this.halfSize; j++) {
				if (boardGet(i, j) == BoardColor.None) {
					if (checkLocValid(i, j, nextPlayer, false)) {
						return nextPlayer;
					}
					
				}
			}
		}
		return currPlayer;
	}

	private boolean checkLocValid(int i, int j, BoardColor currPlayer, boolean doFlip) {
		boolean validLoc = false;
		validLoc |= flip(currPlayer, i, j, decrement, identity, doFlip);
		// Up-Right
		validLoc |= flip(currPlayer, i, j, decrement, increment, doFlip);
		// Right
		validLoc |= flip(currPlayer, i, j, identity, increment, doFlip);
		// Right-Down
		validLoc |= flip(currPlayer, i, j, increment, increment, doFlip);
		// Down
		validLoc |= flip(currPlayer, i, j, increment, identity, doFlip);
		// Down-Left
		validLoc |= flip(currPlayer, i, j, increment, decrement, doFlip);
		// Left
		validLoc |= flip(currPlayer, i, j, identity, decrement, doFlip);
		// Left-Up
		validLoc |= flip(currPlayer, i, j, decrement, decrement, doFlip);
		return validLoc;
	}
	public BoardColor getWinner() {
		return this.winner;
	}
	public BoardColor getCurrPlayer() {
		return this.currPlayer;
	}

}
