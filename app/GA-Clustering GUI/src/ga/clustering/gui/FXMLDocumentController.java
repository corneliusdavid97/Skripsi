/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui;

import ga.clustering.gui.GA.Chromosome;
import ga.clustering.gui.GA.Clusterer;
import ga.clustering.gui.GA.Params;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author CorneliusDavid
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField textFieldDokumen;
   
    @FXML
    private Button buttonDokumen;
    
    @FXML
    private Spinner spinnerCluster;
    
    @FXML
    private Spinner spinnerPopulasi;
    
    @FXML
    private Spinner spinnerMutasi;
    
    @FXML
    private Spinner spinnerMaxIterasi;
    
    @FXML
    private Spinner spinnerElitism;
    
    @FXML
    private Spinner spinnerConvergeGen;
    
    @FXML
    private ChoiceBox choiceBoxWeighting;
    
    @FXML
    private Spinner spinnerConvergeLimit;
    
    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private Button buttonMulai;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //cluster
        SpinnerValueFactory<Integer> clusterValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,2);
        this.spinnerCluster.setValueFactory(clusterValueFactory);
        
        //populasi
        SpinnerValueFactory<Integer> populationValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,5);
        this.spinnerPopulasi.setValueFactory(populationValueFactory);
        
        //mutasi
        SpinnerValueFactory<Double> mutationValueFactory=new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1.0, 0.05, 0.01);
        this.spinnerMutasi.setValueFactory(mutationValueFactory);
        
        //populasi
        SpinnerValueFactory<Integer> iterationValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,100);
        this.spinnerMaxIterasi.setValueFactory(iterationValueFactory);
        
        //weighting
        this.choiceBoxWeighting.setItems(FXCollections.observableArrayList(
                "TF-IDF", "Frekuensi"
        ));
        this.choiceBoxWeighting.getSelectionModel().selectFirst();
        
        //converge boundary
        ObservableList<String> values=FXCollections.observableArrayList(
                "0.0000001","0.000001","0.00001","0.0001","0.001"
        );
        SpinnerValueFactory<String> limitValueFactory=new SpinnerValueFactory.ListSpinnerValueFactory<>(values);
        limitValueFactory.setValue("0.00001");
        this.spinnerConvergeLimit.setValueFactory(limitValueFactory);
        
        //elitism
        SpinnerValueFactory<Integer> elitismValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,1);
        this.spinnerElitism.setValueFactory(elitismValueFactory);
        attachWarningElitism();
        
        //converge Generation
        SpinnerValueFactory<Integer> convergeGenValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,3);
        this.spinnerConvergeGen.setValueFactory(convergeGenValueFactory);
        attachWarningConvergeGen();
    }
    
    private void attachWarningElitism(){
        this.spinnerElitism.valueProperty().addListener((observable, oldValue, newValue) -> {
            if((int)this.spinnerElitism.valueProperty().getValue()>(int)this.spinnerPopulasi.valueProperty().getValue()){
                this.spinnerElitism.getValueFactory().setValue(oldValue);
                try{
                    Skin<?> skin = spinnerElitism.getSkin();
                    Object behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                    behavior.getClass().getMethod("stopSpinning").invoke(behavior);
                }catch(Exception e){}
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Peringatan!");
                alert.setHeaderText("Nilai tidak valid");
                alert.setContentText("Nilai Individu Elitisme tidak bisa lebih besar dari Banyaknya Populasi");

                alert.showAndWait();
            }
        });
    }
    
    private void attachWarningConvergeGen(){
        this.spinnerConvergeGen.valueProperty().addListener((observable, oldValue, newValue) -> {
            if((int)this.spinnerConvergeGen.valueProperty().getValue()>(int)this.spinnerMaxIterasi.valueProperty().getValue()){
                this.spinnerConvergeGen.getValueFactory().setValue(oldValue);
                try{
                    Skin<?> skin = spinnerConvergeGen.getSkin();
                    Object behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                    behavior.getClass().getMethod("stopSpinning").invoke(behavior);
                }catch(Exception e){}
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Peringatan!");
                alert.setHeaderText("Nilai tidak valid");
                alert.setContentText("Banyaknya Generasi Konvergen tidak bisa lebih besar dari Maksimum Iterasi");

                alert.showAndWait();
            }
        });
    }
    
    public void chooseDocument(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File selected=directoryChooser.showDialog(this.buttonDokumen.getScene().getWindow());
        if(selected!=null){
            this.textFieldDokumen.setText(selected.getAbsolutePath());
        }
    }
    
    public void start(){
        if(this.textFieldDokumen.getText().length()==0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan!");
            alert.setHeaderText("Nilai tidak valid");
            alert.setContentText("Direktori untuk dokumen belum dipilih");

            alert.showAndWait();
            return;
        }
        String filepath=this.textFieldDokumen.getText();
        int K=(int)this.spinnerCluster.getValue();
        int P=(int)this.spinnerPopulasi.getValue();
        int weightMethod=this.choiceBoxWeighting.getSelectionModel().getSelectedIndex(); //0=TF-IDF, 1=Frekuensi
        double mu_m=Double.parseDouble(this.spinnerMutasi.getValue().toString());
        int maxIt=(int)this.spinnerMaxIterasi.getValue();
        int elitismCount=(int)this.spinnerElitism.getValue();
        int convergeGen=(int)this.spinnerConvergeGen.getValue();
        double convergeEpsilon=Double.parseDouble(this.spinnerConvergeLimit.getValue().toString());
        
        Params.getInstance().insertParam(filepath, K, P, weightMethod, mu_m, maxIt, elitismCount, convergeGen, convergeEpsilon);
        Thread a=new Thread(Clusterer.getInstance());
        a.start();
    }
}
