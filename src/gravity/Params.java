package gravity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Params {

//    Gravity gravity = new Gravity()
    Stage stP = new Stage();
    private static Data data = new Data();
    private String dens;
    private String vol;
    private String Hs;
    private boolean densOk = false;
    private boolean volOk = false;
    private boolean hsOk = false;
    private boolean nameOk = false;
    
    private TextField name = new TextField();
    private TextField density = new TextField();
    private TextField volume = new TextField();
    private TextField hspeed = new TextField();
    private CheckBox arCheck = new CheckBox();
    private CheckBox spCheck = new CheckBox();
    private CheckBox rcCheck = new CheckBox();
    private ToggleGroup group = new ToggleGroup();
    private Label warningFileOpen = new Label("Загружено с ошибками в файле...");
    
    

    public void assignData(Data data) {
        this.data = data;
    }


    public void addParamsWindow(Group root, CalPath cp, Table table) {
        
        warningFileOpen.setPrefSize(200, 10);
        warningFileOpen.setStyle("-fx-font: 8pt Arial; -fx-text-fill: red;");
        warningFileOpen.setVisible(false);

        VBox vBox = new VBox();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(2);
        gp.setVgap(8);
        gp.setPadding(new Insets(10, 10, 10, 10));
        Pane p = new Pane();
        Rectangle r = new Rectangle();
        r.setWidth(320);
        r.setHeight(30);
        r.setFill(Color.rgb(200, 50, 50, 1));
        Text t = new Text();
        t.setX(35);
        t.setY(22);
        t.setText("Ошибка в параметрах");
        t.setFont(Font.font(null, FontWeight.BOLD, 20));
        p.getChildren().addAll(r, t);
        vBox.getChildren().addAll(gp, warningFileOpen, p);
        p.setVisible(false);

        Label nameLab = new Label("Название");
        gp.add(nameLab, 0, 0);
        
        name.setPromptText("Название для файла");
        name.setMaxWidth(200);
        gp.add(name, 1, 0);
        
        Label denLab = new Label();
        denLab.setText("Плотность:");
        gp.add(denLab, 0, 1);
        
        density.setPromptText("1 - ..");
        density.setMaxWidth(200);
        gp.add(density, 1, 1);

        Label volLab = new Label();
        volLab.setText("Объем:");
        gp.add(volLab, 0, 2);
        
        volume.setPromptText("1 - ..");
        volume.setMaxWidth(200);
        gp.add(volume, 1, 2);

        Label HspeedLab = new Label();
        HspeedLab.setText("Скорость по Х:");
        gp.add(HspeedLab, 0, 3);
        
        hspeed.setPromptText("0 - 50");
        hspeed.setMaxWidth(200);
        gp.add(hspeed, 1, 3);
        

        Label arLab = new Label();
        arLab.setText("Авто реверс:");
        gp.add(arLab, 0, 4);
        
        gp.add(arCheck, 1, 4);

        Label spLab = new Label();
        spLab.setText("Показать путь:");
        gp.add(spLab, 0, 5);
        
        gp.add(spCheck, 1, 5);

        Label rcLab = new Label();
        rcLab.setText("Включить течение:");
        gp.add(rcLab, 0, 6);
        
        gp.add(rcCheck, 1, 6);

        HBox rbBox = new HBox();
        
        Label dtLab = new Label();
        dtLab.setText("Шаг:");
        gp.add(dtLab, 0, 7);
        RadioButton x1 = new RadioButton("x1");
        x1.setId("one");
        x1.setToggleGroup(group);
        x1.setUserData(1);
        x1.setSelected(true);
        RadioButton x2 = new RadioButton("x2");
        x2.setId("two");
        x2.setToggleGroup(group);
        x2.setUserData(2);
        RadioButton x3 = new RadioButton("x3");
        x3.setId("three");
        x3.setToggleGroup(group);
        x3.setUserData(3);
        RadioButton x4 = new RadioButton("x4");
        x4.setId("four");
        x4.setToggleGroup(group);
        x4.setUserData(4);
        rbBox.getChildren().addAll(x1, x2, x3, x4);
        gp.add(rbBox, 1, 7);

        Button ok = new Button("Рассчитать");
        gp.setMargin(ok, new Insets(20, 0, 0, 0));
        gp.add(ok, 0, 8);
        
        
        root.getChildren().add(vBox);
//        Scene scene = new Scene(root, 320, 300);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.setTitle("Парметры шара");
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initOwner(st);
//        stP = stage;

        Pattern firstNullPattern = Pattern.compile("^0[0-9]?+$");//число с первым нулем но без точки
        Pattern fromNullToFifthyPattern = Pattern.compile("^([0-9]|[1-4][0-9]|50)$");//число от 0 до 50
        Pattern emptyPattern = Pattern.compile("^$");//пустое поле
        Pattern niceFilePattern = Pattern.compile("^\\w+$");//допустимый ввод для фала
        Pattern allNumsPattern = Pattern.compile("^\\d*$");//сколько угодно цифровых символов
        Pattern fractionalPattern = Pattern.compile("^[0-9]+\\.[0-9]+$");//дробные числа
        
        name.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue <? extends String> ov, String oldV,
                    String newV){
                Matcher nfpm = niceFilePattern.matcher(newV);
                if(nfpm.matches()){
                    name.setStyle("-fx-text-inner-color: black;");
                nameOk = true;
                } else {
                    nameOk = false;
                    name.setStyle("-fx-text-inner-color: red;");
                }
            }
        });

        density.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String old_val,
                    String new_val) {
                Matcher anm = allNumsPattern.matcher(new_val);
                Matcher fnpm = firstNullPattern.matcher(new_val);
                Matcher em = emptyPattern.matcher(new_val);
                Matcher fm = fractionalPattern.matcher(new_val);
                if (anm.matches() & !em.matches() & !fnpm.matches() & !fm.matches()) {
                    density.setStyle("-fx-text-inner-color: black;");
                    dens = new_val;
                    densOk = true;

                } else {
                    density.setStyle("-fx-text-inner-color: red;");
                    densOk = false;
                }
            }
        });
        volume.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String old_val,
                    String new_val) {
                Matcher anm = allNumsPattern.matcher(new_val);
                Matcher fnpm = firstNullPattern.matcher(new_val);
                Matcher em = emptyPattern.matcher(new_val);
                Matcher fm = fractionalPattern.matcher(new_val);
                if (anm.matches() & !em.matches() & !fnpm.matches() & !fm.matches()) {
                    volume.setStyle("-fx-text-inner-color: black;");
                    vol = new_val;
                    volOk = true;
                } else {
                    volume.setStyle("-fx-text-inner-color: red;");
                    volOk = false;
                }

            }
        });
        hspeed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String old_val,
                    String new_val) {
                Matcher fntfm = fromNullToFifthyPattern.matcher(new_val);
//                Matcher fnpm = firstNullPattern.matcher(new_val);
                Matcher em = emptyPattern.matcher(new_val);
                Matcher fm = fractionalPattern.matcher(new_val);
                if (fntfm.matches() & !em.matches()  & !fm.matches()) {
                    
                    hspeed.setStyle("-fx-text-inner-color: black;");
                    Hs = new_val;
                    hsOk = true;
                } else {
                    hspeed.setStyle("-fx-text-inner-color: red;");
                    hsOk = false;
                }

            }
        });
        spCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val,
                    Boolean new_val) {
                data.setShowPath(new_val);
            }
        });
        arCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val,
                    Boolean new_val) {
                data.setAutoReverse(new_val);
            }
        });
        rcCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val,
                    Boolean new_val) {
                data.setRiverC(new_val);
            }
        });
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle,
                    Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    data.setDt((int) group.getSelectedToggle().getUserData());

                }
            }
        });

        ok.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (checkConditions()) {
                    warningFileOpen.setVisible(false);
                    data.clearMap();
                    data.setP(Double.parseDouble(dens));
                    data.setV(Double.parseDouble(vol));
                    data.setHspeed(Double.parseDouble(Hs));
                    data.setDataChanged(true);
                    cp.setAllVar();
                    data.clearSecToWater();
                    cp.calcCoords();
                    table.refreshTable();
                    t.setText("Расчет произведен");
                }
            }
        });
        ok.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (checkConditions()) {
                    r.setWidth(320);
                    r.setHeight(30);
                    r.setFill(Color.rgb(50, 200, 50, 1));
                    t.setX(40);
                    t.setY(22);
                    t.setText("Можно продолжать");
                    t.setFont(Font.font(null, FontWeight.BOLD, 20));
                    p.setVisible(true);
                } else {
                    r.setWidth(320);
                    r.setHeight(30);
                    r.setFill(Color.rgb(200, 50, 50, 1));
                    t.setX(35);
                    t.setY(22);
                    t.setText("Ошибка в параметрах");
                    t.setFont(Font.font(null, FontWeight.BOLD, 20));
                    p.setVisible(true);
                }
            }
        });
        ok.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                p.setVisible(false);
            }
        });

    }
    
    public boolean checkConditions(){
        if(densOk & volOk & hsOk & nameOk){
            return true;
        } else {
            return false;
        }
    }
    
    public Label getWFO(){
        return warningFileOpen;
    }
    
    public TextField getName(){
        return name;
    }
    public TextField getDensity(){
        return density;
    }
    public TextField getVolume(){
        return volume;
    }
    public TextField getHspeed(){
        return hspeed;
    }
    public CheckBox getArCheck(){
        return arCheck;
    }
    public CheckBox getSpCheck(){
        return spCheck;
    }
    public CheckBox getRcCheck(){
        return rcCheck;
    }
    public ToggleGroup getGroup(){
        return group;
    }
    
    public void setDensOk(boolean b){
        densOk = b;
    }
    public void setVolOk(boolean b){
        volOk = b;
    }
    public void setHsOk(boolean b){
        hsOk = b;
    }
    public void setNameOk(boolean b){
        nameOk = b;
    }


}
