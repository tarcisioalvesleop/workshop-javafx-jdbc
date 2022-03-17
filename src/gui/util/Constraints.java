package gui.util;

import javafx.scene.control.TextField;

public class Constraints {
	//txt aceita só numeros inteiros
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) ->{
			if(newValue != null && !newValue.matches("\\d*")) {//\\d* valor de numero inteiro(expressao regular)
				txt.setText(oldValue);
			}
		});
	}
	//txt aceita só numeros menor que max
	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if(newValue != null && newValue.length() > max) {
				txt.setText(oldValue);
			}
		});
	}
	
	//txt aceita apenas numeros double
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if(newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {//valor flutuante
				txt.setText(oldValue);
			}
		});
	}
	
}
