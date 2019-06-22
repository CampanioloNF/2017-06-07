/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<String> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;
	private Season stagione = null;

    @FXML
    void handleCarica(ActionEvent event) {

    	txtResult.clear();
    	stagione  = boxSeason.getValue();
    	
    	if(stagione!=null) {
    		
    	   model.creaGrafo(stagione.getSeason());
    	   txtResult.appendText("Classifica "+stagione+":\n\n");
    	   for(Team t : model.getClassifica())
    		   txtResult.appendText(t.getTeam()+" punti: "+t.getPunteggio()+"\n");
    	   
    	   boxTeam.setItems(FXCollections.observableList(model.getVertici()));
    	   
    	}else {
    		txtResult.appendText("Si prega di scegliere una season.");
    		return;
    	}
    	
    }

    @FXML
    void handleDomino(ActionEvent event) {

    	txtResult.clear();
    	
    	if(stagione!=null) {
    		
    		if(stagione==boxSeason.getValue()) {
    			
    			if(boxTeam.getValue()!=null) {
    				
    				List<Team> cammino = model.cercaCammino(boxTeam.getValue());
    				if(cammino.size()>1) {
    					
    					for(Team t : cammino)
    					   txtResult.appendText(t.getTeam()+"\n");
    				}
    				else {
    					txtResult.appendText("Questa squadra non ha vinto una partita..");
    					return;
    				}
    				
    			}else {
            		txtResult.appendText("Si prega di selezionare una squadra");
            		return;
            	}
    			
    			
    		}else {
        		txtResult.appendText("Se si volesse cambiare stagione stagione, premere 'Carica partite'");
        		return;
        	}
    		
    		
    	}else {
    		txtResult.appendText("Si prega di selezionare una stagione e premere 'Carica partite'");
    		return;
    	}
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }

	public void setModel(Model model) {
		this.model = model;		
	}

	public void caricaSeason() {
    	boxSeason.getItems().addAll(model.getSeason());
	}
}
