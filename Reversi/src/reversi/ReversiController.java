package reversi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import reversi.GameModel.BoardColor;

public class ReversiController {
	private FrontendGameModel game;
	private int prevSize = 0;
	
	public ReversiController(){
	}
	@FXML
	private ImageView currPlayerImg;
	@FXML
	private Text currentPlayer;
	@FXML
	private GridPane grid;
	
	
	@FXML
	private Button restart;
	@FXML
	private Button exit;
	@FXML
	private Text winnerText;
	@FXML
	private Rectangle background;
	private Stage stage;
	
	public void gameStart(int size) {
		// TODO Auto-generated method stub
		grid.getChildren().clear();
		this.game = new FrontendGameModel(this, size, grid, currPlayerImg);
		this.prevSize = size;
		setPanelVisible(false);
	}

	private void setPanelVisible(boolean b) {
		background.setDisable(!b);
		restart.setDisable(!b);
		exit.setDisable(!b);
		winnerText.setDisable(!b);
		background.setVisible(b);
		restart.setVisible(b);
		exit.setVisible(b);
		winnerText.setVisible(b);
	}

	public void gameEnd(BoardColor winner) {
		setPanelVisible(true);
		winnerText.setText(game.getWinner().toString() + " is winner. ");
	}
	@FXML
	public void gameExit(ActionEvent event) {
		stage.close();
	}
	@FXML
	public void gameRestart(ActionEvent event) {
		gameStart(this.prevSize);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
