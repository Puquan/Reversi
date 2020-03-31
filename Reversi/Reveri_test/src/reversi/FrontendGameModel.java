package reversi;


import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class FrontendGameModel extends GameModel {
	private Image imageNone;
	private Image imageBlack;
	private Image imageWhite;
	private GridPane grid;
	private ImageView currPlayerImg;
	private List<List<ImageView>> nodeBoard;
	private ReversiController controller;
	private BoardColor currColor; 
	
	public FrontendGameModel(ReversiController controller, int halfSize, GridPane grid, ImageView currPlayerImg) {
		super(halfSize);
		this.grid = grid;
		this.currPlayerImg = currPlayerImg;
		this.nodeBoard = new ArrayList<>();
		this.controller = controller;
		this.imageNone = new Image("file:/../images/none.png");
		this.imageBlack = new Image("file:/../images/black.png");
		this.imageWhite = new Image("file:/../images/white.png");
		
		this.currColor = BoardColor.Black;
		
		for (int i = 0; i < 2 * halfSize; i++) {
			nodeBoard.add(new ArrayList<ImageView>());
			for (int j = 0; j < 2 * halfSize; j++) {
				ImageView newLoc = new ImageView(imageNone);
				nodeBoard.get(i).add(newLoc);
				this.grid.add(newLoc, i, j);
				int x = i;
				int y = j;
				newLoc.setOnMouseClicked(new EventHandler<MouseEvent>() { 
			       @Override 
			       public void handle(MouseEvent event) {
			    	   if (moves(x, y)) {
			    	   		if (currColor == BoardColor.Black) {
			    	   			currColor = BoardColor.White;
			    	   		} else {
			    	   			currColor = BoardColor.Black;
			    	   		}
			    	   	}			    	   	
			       }
				});
			}
		}
		
		movePiece(halfSize-1, halfSize-1, BoardColor.Black);
		movePiece(halfSize-1, halfSize, BoardColor.White);
		movePiece(halfSize, halfSize-1, BoardColor.White);
		movePiece(halfSize, halfSize, BoardColor.Black);
		this.currPlayerImg.setImage(getImg(this.getCurrPlayer()));
	}
	private Image getImg(BoardColor color) {
		if (color == BoardColor.Black) {
			return imageBlack;
		} else if (color == BoardColor.White) {
			return imageWhite;
		} else if (color == BoardColor.None) {
			return imageNone;
		} else {
			return null;
		}
	}
	@Override
	public void movePiece(int i, int j, BoardColor color) {
		Image currImage = getImg(color);
		nodeBoard.get(i).get(j).setImage(currImage);
	}
	@Override
	public void updateCurrentPlayer() {
		this.currPlayerImg.setImage(getImg(this.getCurrPlayer()));
	}
	@Override
	public void gameStop() {
		controller.gameEnd(this.getWinner());
	}
}
