package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;

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
		//Chamando a view que desejo abrir, com ação de inicialização do departmentListController como parametro lambda
		loadView("/gui/DepartmentList.fxml", (DepartmentListController 
			controller) -> {
			controller.setDepartmentService(new DepartmentService());//função foi parametrizada no metodo loadView
			controller.updateTableView();//função foi parametrizada no metodo loadView
		} );
	}
	@FXML
	public void onMenuItemAboutAction() {
		//Chamando a view que desejo abrir 
		loadView("/gui/About.fxml", x -> {});
	}
	
	//metodo da initialize
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {//sinchronized não permite interrupçao nesse método
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
			
			//inicializando o consumer (função parametrizada como argumento na chamada do metodo)
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	/*
	private synchronized void loadView2(String absoluteName) {//sinchronized não permite interrupçao nesse método
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
			
			DepartmentListController controller = loader.getController();//acessando loader
			controller.setDepartmentService(new DepartmentService());//injetando dependencia no controller
			controller.updateTableView();//atualizando os dados na tela do table view
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}*/

}
