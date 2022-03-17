package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	//atributos dos itens menu
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	//métodos tratando os eventos do menu 
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	@FXML
	public void onMenuItemDepartmentAction() {
		//Chamando a view que desejo abrir
		loadView("/gui/DepartmentList.fxml");
	}
	@FXML
	public void onMenuItemAboutAction() {
		//Chamando a view que desejo abrir 
		loadView("/gui/About.fxml");
	}
	
	//metodo da initialize
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized void loadView(String absoluteName) {//sinchronized não permite interrupçao nesse método
		try {
			//abrindo uma nova janela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			//pegando o metodo getMainScene() da class Main
			Scene mainScene = Main.getMainScene();
			//pegando uma referencia do VBox que esta na janela principal MainView
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			//salvando o filho da janela principal
			Node mainmenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainmenu);//incluindo os menus do mainScene
			mainVBox.getChildren().addAll(newVBox.getChildren());//incluindo os filhos da janela nova
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
