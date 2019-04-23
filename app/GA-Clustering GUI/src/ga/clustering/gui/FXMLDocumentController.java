/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui;

import ga.clustering.gui.GA.Chromosome;
import ga.clustering.gui.GA.Clusterer;
import ga.clustering.gui.GA.Params;
import ga.clustering.gui.IR.Document;
import ga.clustering.gui.KMeans.KMeans;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

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
    
    @FXML
    private TextField textFieldHasil;
    
    @FXML
    private Button buttonHasil;
    
    @FXML
    private Tab tabGA;
    
    @FXML
    private Tab tabKMeans;
    
    @FXML
    private Label labelProgress;
    
    //KMEANS
    @FXML
    private Spinner KMspinnerCluster;
    
    @FXML
    private ChoiceBox KMchoiceBoxWeighting;
    
    @FXML
    private Spinner KMspinnerMaxIterasi;
    
    @FXML
    private ProgressBar KMprogressBar;
    
    @FXML
    private Button KMbuttonMulai;
    
    private Thread thread;
    
    private long runningTime;
    
    private int iteration;
    
    private Task task;
    
    private int curIt;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //hasil
        File initial=new File("res\\");
        this.textFieldHasil.setText(initial.getAbsolutePath());
        
        //cluster
        SpinnerValueFactory<Integer> clusterValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,5);
        this.spinnerCluster.setValueFactory(clusterValueFactory);
        
        //populasi
        SpinnerValueFactory<Integer> populationValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,100);
        this.spinnerPopulasi.setValueFactory(populationValueFactory);
        
        //mutasi
        SpinnerValueFactory<Double> mutationValueFactory=new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1.0, 0.05, 0.01);
        this.spinnerMutasi.setValueFactory(mutationValueFactory);
        
        //max iterasi
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
        attachWarning();
        
        //converge Generation
        SpinnerValueFactory<Integer> convergeGenValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,3);
        this.spinnerConvergeGen.setValueFactory(convergeGenValueFactory);
        
        //KMEANS
        //cluster
        SpinnerValueFactory<Integer> KMclusterValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,5);
        this.KMspinnerCluster.setValueFactory(KMclusterValueFactory);
        
        //weighting
        this.KMchoiceBoxWeighting.setItems(FXCollections.observableArrayList(
                "TF-IDF", "Frekuensi"
        ));
        this.KMchoiceBoxWeighting.getSelectionModel().selectFirst();
        
        //max iterasi
        SpinnerValueFactory<Integer> KMiterationValueFactory =new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE,100);
        this.KMspinnerMaxIterasi.setValueFactory(KMiterationValueFactory);
    }
    
    private void attachWarning(){
        this.spinnerElitism.valueProperty().addListener((observable, oldValue, newValue) -> warningPopulation(observable, oldValue, newValue));
        this.spinnerPopulasi.valueProperty().addListener((observable, oldValue, newValue) -> warningPopulation(observable, oldValue, newValue));
        this.spinnerConvergeGen.valueProperty().addListener((observable, oldValue, newValue) -> warningIteration(observable, oldValue, newValue));
        this.spinnerMaxIterasi.valueProperty().addListener((observable, oldValue, newValue) -> warningIteration(observable, oldValue, newValue));
    }
    
    private void warningPopulation(Object observable, Object oldValue, Object newValue){
        if((int)this.spinnerElitism.valueProperty().getValue()>(int)this.spinnerPopulasi.valueProperty().getValue()){
            this.spinnerElitism.getValueFactory().setValue(oldValue);
            this.spinnerPopulasi.getValueFactory().setValue(oldValue);
            try{
                Skin<?> skin = spinnerElitism.getSkin();
                Object behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                behavior.getClass().getMethod("stopSpinning").invoke(behavior);
                
                skin = spinnerPopulasi.getSkin();
                behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                behavior.getClass().getMethod("stopSpinning").invoke(behavior);
            }catch(Exception e){}
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan!");
            alert.setHeaderText("Nilai tidak valid");
            alert.setContentText("Nilai Individu Elitisme tidak bisa lebih besar dari Banyaknya Populasi");

            alert.showAndWait();
        }
    }
    
    private void warningIteration(Object observable, Object oldValue, Object newValue){
        if((int)this.spinnerConvergeGen.valueProperty().getValue()>(int)this.spinnerMaxIterasi.valueProperty().getValue()){
            this.spinnerConvergeGen.getValueFactory().setValue(oldValue);
            this.spinnerMaxIterasi.getValueFactory().setValue(oldValue);
            try{
                Skin<?> skin = spinnerConvergeGen.getSkin();
                Object behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                behavior.getClass().getMethod("stopSpinning").invoke(behavior);
                
                skin = spinnerMaxIterasi.getSkin();
                behavior = skin.getClass().getMethod("getBehavior").invoke(skin);
                behavior.getClass().getMethod("stopSpinning").invoke(behavior);
            }catch(Exception e){}
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan!");
            alert.setHeaderText("Nilai tidak valid");
            alert.setContentText("Banyaknya Generasi Konvergen tidak bisa lebih besar dari Maksimum Iterasi");

            alert.showAndWait();
        }
    }
    
    public void chooseDocument(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File selected=directoryChooser.showDialog(this.buttonDokumen.getScene().getWindow());
        if(selected!=null){
            this.textFieldDokumen.setText(selected.getAbsolutePath());
        }
    }
    
    public void chooseResult(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File initial=new File("res\\");
        if(!initial.exists()){
            initial.mkdirs();
        }
        directoryChooser.setInitialDirectory(initial);
        File selected=directoryChooser.showDialog(this.buttonHasil.getScene().getWindow());
        if(selected!=null){
            this.textFieldHasil.setText(selected.getAbsolutePath());
        }
    }
    
    private void stop(){
        //disable all
        this.buttonDokumen.disableProperty().set(false);
        this.spinnerCluster.disableProperty().set(false);
        this.spinnerConvergeGen.disableProperty().set(false);
        this.spinnerConvergeLimit.disableProperty().set(false);
        this.spinnerElitism.disableProperty().set(false);
        this.spinnerMaxIterasi.disableProperty().set(false);
        this.spinnerMutasi.disableProperty().set(false);
        this.spinnerPopulasi.disableProperty().set(false);
        this.choiceBoxWeighting.disableProperty().set(false);
        this.textFieldDokumen.disableProperty().set(false);
        this.textFieldHasil.disableProperty().set(false);
        this.buttonHasil.disableProperty().set(false);
        this.buttonMulai.setText("Mulai");
    }
    
    public void reset(){
        this.buttonDokumen.disableProperty().set(false);
        this.spinnerCluster.disableProperty().set(false);
        this.spinnerConvergeGen.disableProperty().set(false);
        this.spinnerConvergeLimit.disableProperty().set(false);
        this.spinnerElitism.disableProperty().set(false);
        this.spinnerMaxIterasi.disableProperty().set(false);
        this.spinnerMutasi.disableProperty().set(false);
        this.spinnerPopulasi.disableProperty().set(false);
        this.choiceBoxWeighting.disableProperty().set(false);
        this.textFieldDokumen.disableProperty().set(false);
        this.textFieldHasil.disableProperty().set(false);
        this.buttonHasil.disableProperty().set(false);
        this.buttonMulai.setText("Mulai");
    }
    
    public void start() throws Exception{
        if(this.buttonMulai.getText().equalsIgnoreCase("reset")){
            reset();
            return;
        }
        
        if(this.buttonMulai.getText().equalsIgnoreCase("berhenti")){
            task.cancel();
            return;
        }
        
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
        
        curIt=0;
        Params.getInstance().insertParam(filepath, K, P, weightMethod, mu_m, maxIt, elitismCount, convergeGen, convergeEpsilon);
        task=new Task() {
            @Override
            protected Object call() throws Exception {
                Clusterer.getInstance().progressProperty().addListener((obs, oldProgress, newProgress) -> {
                    if(newProgress.doubleValue()==1){
                        updateMessage(String.format("Generasi %d/%d",++curIt,maxIt));
                    }
                    updateProgress(newProgress.doubleValue(), 1);
                });
                updateMessage("Inisialisasi...");
                Clusterer.getInstance().initialize();
                updateMessage("Generasi 0/"+maxIt);
                updateProgress(0, 100);
                Clusterer.getInstance().getClusteringTask();
                return true;
            }
        };
        this.progressBar.progressProperty().bind(task.progressProperty());
        this.labelProgress.textProperty().bind(task.messageProperty());
        
        //disable all
        this.buttonDokumen.disableProperty().set(true);
        this.spinnerCluster.disableProperty().set(true);
        this.spinnerConvergeGen.disableProperty().set(true);
        this.spinnerConvergeLimit.disableProperty().set(true);
        this.spinnerElitism.disableProperty().set(true);
        this.spinnerMaxIterasi.disableProperty().set(true);
        this.spinnerMutasi.disableProperty().set(true);
        this.spinnerPopulasi.disableProperty().set(true);
        this.choiceBoxWeighting.disableProperty().set(true);
        this.textFieldDokumen.disableProperty().set(true);
        this.textFieldHasil.disableProperty().set(true);
        this.buttonHasil.disableProperty().set(true);
        this.buttonMulai.setText("Berhenti");
        
        thread=new Thread(task);
        long currentTime=System.currentTimeMillis();
        
        thread.start();
        task.setOnSucceeded((event) -> {
            this.buttonMulai.setText("Reset");
            this.runningTime=System.currentTimeMillis()-currentTime;
            writeToFile("GA",Clusterer.getInstance().getSolution().getClusteringResult(), Clusterer.getInstance().getSolution().getFitness());
            this.labelProgress.textProperty().unbind();
            this.labelProgress.setText("");
        });
        
        task.setOnCancelled((event) -> {
            task.cancel();
            stop();
            this.labelProgress.textProperty().unbind();
            this.labelProgress.setText("");
        });
    }
    
    
    public void KMStart(){
        if(this.textFieldDokumen.getText().length()==0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan!");
            alert.setHeaderText("Nilai tidak valid");
            alert.setContentText("Direktori untuk dokumen belum dipilih");

            alert.showAndWait();
            return;
        }
        String filepath=this.textFieldDokumen.getText();
        int K=(int)this.KMspinnerCluster.getValue();
        int weightMethod=this.KMchoiceBoxWeighting.getSelectionModel().getSelectedIndex(); //0=TF-IDF, 1=Frekuensi
        int maxIt=(int)this.KMspinnerMaxIterasi.getValue();
        
        Params.getInstance().insertParam(filepath, K, -1, weightMethod, -1, maxIt, -1, -1, -1);
        task=new Task() {
            @Override
            protected Object call() throws Exception {
                KMeans.getInstance().progressProperty().addListener((obs, oldProgress, newProgress) -> {
                    if(newProgress.doubleValue()==1){
                        updateMessage(String.format("Generasi %d/%d",++curIt,maxIt));
                    }
                    updateProgress(newProgress.doubleValue(), 1);
                });
                KMeans.getInstance().cluster();
                return true;
            }
        };
        this.KMprogressBar.progressProperty().bind(task.progressProperty());
        
        this.KMchoiceBoxWeighting.disableProperty().set(true);
        this.KMspinnerCluster.disableProperty().set(true);
        this.KMspinnerMaxIterasi.disableProperty().set(true);
        
        long currentTime = System.currentTimeMillis();
        task.setOnSucceeded((event) -> {
            this.KMbuttonMulai.setText("Reset");
            this.runningTime=System.currentTimeMillis()-currentTime;
            writeToFile("KMeans",KMeans.getInstance().getLast(), KMeans.getInstance().getLastIntracluster());
            this.labelProgress.textProperty().unbind();
            this.labelProgress.setText("");
        });
        thread=new Thread(task);
        thread.start();
    }
    
    private String timeFormatter(long timeMillis){
        int timeSecond=(int)timeMillis/1000;
        int hour=timeSecond/3600;
        timeSecond=timeSecond%3600;
        int minutes=timeSecond/60;
        timeSecond=timeSecond%60;
        int second=timeSecond;
        
        String result="";
        result+=hour>0?hour+" jam ":"";
        result+=minutes>0?minutes+" menit ":"";
        result+=second+" detik";
        return result;
    }
    
    private void writeToFile(String mode, HashMap<Document, Integer> clusteringResult, double fitness){
        try{
            System.out.println("writing...");
            String filepath=Params.getInstance().getFilepath();
            List<Document>[] result=new ArrayList[Params.getInstance().getK()];
            for (int i = 0; i < result.length; i++) {
                result[i]=new ArrayList<>();
            }
            for(Entry<Document,Integer> entry:clusteringResult.entrySet()){
                result[entry.getValue()].add(entry.getKey());
            }
            int maxLength=0;
            for (int i = 0; i < result.length; i++) {
                maxLength=Math.max(maxLength, result[i].size());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH_mm_ss");
            Date date = new Date();
            String pathHasil=textFieldHasil.getText();
            String filename=pathHasil+"\\"+mode+"-"+dateFormat.format(date)+".csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            
            //write header
            bw.write("Direktori Dokumen:,"+filepath+"\r\n \r\n");
            bw.write("Parameter: \r\n");
            if(Params.getInstance().getK()!=-1)bw.write("Banyaknya Cluster,"+Params.getInstance().getK()+"\r\n");
            if(Params.getInstance().getP()!=-1)bw.write("Banyaknya Populasi,"+Params.getInstance().getP()+"\r\n");
            if(Params.getInstance().getWeightMethod()!=-1)bw.write("Metode Pembobotan,"+(Params.getInstance().getWeightMethod()==0?"TF-IDF":"Frekuensi")+"\r\n");
            if(Params.getInstance().getMu_m()!=-1)bw.write("Probabilitas Mutasi,"+Params.getInstance().getMu_m()+"\r\n");
            if(Params.getInstance().getMaxIt()!=-1)bw.write("Maksimum Iterasi,"+Params.getInstance().getMaxIt()+"\r\n");
            if(Params.getInstance().getElitismCount()!=-1)bw.write("Individu Elitisme,"+Params.getInstance().getElitismCount()+"\r\n");
            if(Params.getInstance().getConvergeGen()!=-1)bw.write("Banyaknya Generasi Konvergen,"+Params.getInstance().getConvergeGen()+"\r\n");
            if(Params.getInstance().getConvergeEpsilon()!=-1)bw.write("Batas Konvergen,"+Params.getInstance().getConvergeEpsilon()+"\r\n \r\n");
            
            //write hasil
            bw.write("Hasil: \r\n");
            bw.write("Waktu,"+timeFormatter(runningTime)+"\r\n");
            bw.write("Intracluster,"+fitness+"\r\n");
            bw.write("Banyak Iterasi,"+iteration+"\r\n \r\n");
            
            //write hasil clustering
            bw.write("Hasil Clustering: \r\n");
            for (int i = 0; i < result.length; i++) {
                bw.write("C"+(i+1)+",");
            }
            bw.write("\r\n");
            for (int i = 0; i < maxLength; i++) {
                for (int j = 0; j < result.length; j++) {
                    if(i<result[j].size()){
                        String name=result[j].get(i).getDocName().substring(filepath.length());
                        if(name.charAt(0)=='\\'){
                            name=name.substring(1);
                        }
                        bw.write(name);
                    }
                    bw.write(",");
                }
                bw.write("\r\n");
            }
            bw.close();
        }catch(Exception e){}
        System.out.println("written");
    }
}
