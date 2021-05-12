
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    private ComboBox<Country> cmbCountry;
    
    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	String annoS=txtAnno.getText();
    	int anno;
    	if(annoS.length()==0) {
    		txtResult.setText("Anno non inserito");
    		return;
    	}
    	
    	try {
    		anno=Integer.parseInt(annoS);
    		if(anno<1816 || anno>2016) {
    			txtResult.setText("L'anno deve essere compreso tra il 1816 e il 2016");
        		return;
    		}
    	} catch (NumberFormatException e) {
    		txtResult.setText("Formato anno non valido");
    		return;
    	}
    	
    	model.creaGrafo(anno);
    	
    	
    	txtResult.setText("Grafo creato con "+model.getGrafo().vertexSet().size()+" vertici e "+model.getGrafo().edgeSet().size()+" archi.\n");
    	txtResult.appendText("Numero componenti connesse: "+model.getNumConnessioni());
    	for(Country c:model.getGrafo().vertexSet()) {
    		txtResult.appendText("\n"+c.getStateNme()+" "+model.getGrafo().degreeOf(c));
    	}
    	
    	cmbCountry.getItems().addAll(model.getGrafo().vertexSet());
    }
    
    @FXML
    void doRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	Country country=cmbCountry.getValue();
    	
    	if(country==null) {
    		txtResult.setText("Selezionare una Nazione");
    		return;
    	}
    	
    	List<Country> paesi=model.getStatiraggiungibili(country);
    	if(paesi.size()==1) {
    		txtResult.setText("La nazione non ha stati ragiungibili");
    		return;
    	}
    	txtResult.setText("Stati raggiungibili: "+paesi.size());
    	for(int i=0; i<paesi.size(); i++)
    		txtResult.appendText("\n"+paesi.get(i).getStateNme());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCountry != null : "fx:id=\"cmbCountry\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
