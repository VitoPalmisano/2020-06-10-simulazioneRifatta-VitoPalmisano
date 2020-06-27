/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.scene.shape.BoxHelper.BoxAccessor;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	if(boxAttore.getValue()==null) {
    		txtResult.setText("Selezionare un attore dalla tendina");
    		return;
    	}
    	
    	txtResult.setText("Attori simili a: "+boxAttore.getValue());
    	
    	for(Actor a : model.getRaggiungibili(boxAttore.getValue())) {
    		txtResult.appendText("\n"+a);
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	    
    	if(boxGenere.getValue()==null) {
    		txtResult.setText("Selezionare un genere dalla tendina");
    		return;
    	}
    	
    	model.creaGrafo(boxGenere.getValue());
    	
    	txtResult.setText("Creato grafo con "+model.getNumVertici()+" vertici "+model.getNumArchi()+" archi");
    	
    	boxAttore.getItems().addAll(model.getVertici());
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	
    	int ng;
    	
    	try {
			ng = Integer.parseInt(txtGiorni.getText());
		}catch(NumberFormatException nfe) {
			txtResult.setText("Inserire un numero intero");
			return;
		}
    	
    	model.simulazione(ng);
    	
    	txtResult.setText("Il produttore in "+ng+" giorni, ha effettuato "+model.getNumPause()+" pause ed ha intervistato: ");
    	
    	for(Actor a : model.getIntervistati()) {
    		txtResult.appendText("\n"+a);
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxGenere.getItems().addAll(model.getGeneri());
    }
}
