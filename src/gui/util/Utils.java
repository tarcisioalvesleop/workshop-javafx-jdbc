package gui.util;



import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) {
		//acessando o controle onde o stage est�
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
