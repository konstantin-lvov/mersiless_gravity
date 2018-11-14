package gravity;

import static javafx.animation.Animation.Status.PAUSED;
import static javafx.animation.Animation.Status.STOPPED;
import javafx.animation.PathTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author fromchaos
 */
public class Graph {
    
    private static Data data;
    private static Circle c = new Circle();
    private GridPane infoTable = new GridPane();
    private Label maxVWater = new Label();
    private Label currentV = new Label();
    private Label maxVAir = new Label();
    private Label secToWater = new Label();
    private Label finishC = new Label();
    Group groupScale = new Group();
    Group root;
    
    
    
    private SimpleBooleanProperty booleanPropStart;
    private static Path mainPath;
    private static CalPath cp;
    private static Pane pane;
    private static PathTransition transition = new PathTransition();
    private static double opacity = 0;
    
    Graph(Data data, Group root, CalPath cp){
        this.data = data;
        this.root = root;
        this.cp = cp;
    }
    
    //создаем путь для шара используя класс CalculatePath
    private Path generateCurvyPath(final double pathOpacity) {
        byte canCCT = 0;
        boolean missOneStep = false;
        final Path path = new Path();
        path.getElements().add(new MoveTo(150, 50));
//        data.clearSecToWater();
        secToWater.setText("--");
        
//        cp.calcCoords();
        for (int i = 0; i < cp.getCoordX().size(); i++) {
            path.getElements().add(new LineTo((double) cp.getCoordX().get(i),
                    (double) cp.getCoordY().get(i)));
            
        }
        
        path.setOpacity(pathOpacity);
        return path;
    }

    //создаем перемещение для фигуры по полученому пути
    private PathTransition generatePathTransition(final Shape shape, final Path path) {
        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds((cp.getCoordY().size()) / data.getDt()));
        pathTransition.setDelay(Duration.seconds(1));
        pathTransition.setNode(shape);
        pathTransition.setPath(path);
        
        pathTransition.setAutoReverse(data.isAutoReverse());
        if (pathTransition.isAutoReverse()) {
            pathTransition.setCycleCount(2);
        }
        
        return pathTransition;
    }
    
    //метод для запуска анимации
    public void animationConstructor() {
        cp.setAllVar();
        if (data.isShowPath()) {
            opacity = 0.5;
        } else {
            opacity = 0;
        }
        if(mainPath != null){
        mainPath.setVisible(false);
        }
        
        mainPath = generateCurvyPath(opacity);//прозрачность в конструкторе
        root.getChildren().add(mainPath);
        transition = generatePathTransition(c, mainPath);
    }
    
    public void addGraphWindow(){
        
        Button play = new Button("Play");
        play.setLayoutX(550);
        play.setLayoutY(400);
        Button stop = new Button("Pause");
        stop.setLayoutX(600);
        stop.setLayoutY(400);
        Pane alertPane = new Pane();
        alertPane.setLayoutX(245);
        alertPane.setLayoutY(400);
        Rectangle alertR = new Rectangle();
        Text alertT = new Text();
        
        alertR.setWidth(300);
        alertR.setHeight(26);
        alertR.setFill(Color.rgb(200, 50, 50, 1));
        
        alertT.setX(35);
        alertT.setY(20);
        alertT.setText("Задайте параметры");
        alertT.setFont(Font.font(null, FontWeight.BOLD, 20));
        alertPane.getChildren().addAll(alertR, alertT);
        alertPane.setVisible(false);
        
         c = new Circle(150, 50, 1);
        c.setFill(Color.rgb(20, 250, 20));
        c.setRadius(6);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        c.setEffect(shadow);
        Rectangle r = new Rectangle();
        r.setFill(Color.rgb(100, 100, 200, 0.7));
        r.setLayoutX(0);
        r.setLayoutY(250);
        r.setWidth(750);
        r.setHeight(235);
        
        infoTable.setHgap(2);
        infoTable.setVgap(4);
        infoTable.setLayoutX(450);
        infoTable.setLayoutY(20);
        Label currentVLAB = new Label();
        currentVLAB.setText("Текущая V: ");
        Label maxVAirLAB = new Label();
        maxVAirLAB.setText("Макс. V в воздухе: ");
        Label maxVWaterLAB = new Label();
        maxVWaterLAB.setText("Макс. V в воде: ");
        Label secToWaterLAB = new Label("Время падения в воздухе: ");
        //следующие три ноды иницциализированы как поля
        currentV.setText(String.valueOf((int) data.getcurrentV()));
        maxVAir.setText(String.valueOf((int) data.getmaxVAir()));
        maxVWater.setText(String.valueOf((int) data.getmaxVWater()));
        secToWater.setText("0");
        
        infoTable.add(currentVLAB, 0, 0);
        infoTable.add(maxVAirLAB, 0, 1);
        infoTable.add(maxVWaterLAB, 0, 2);
        infoTable.add(secToWaterLAB, 0, 3);
        infoTable.add(currentV, 1, 0);
        infoTable.add(maxVAir, 1, 1);
        infoTable.add(maxVWater, 1, 2);
        infoTable.add(secToWater, 1, 3);
        
        
        finishC.setVisible(false);
        
        int array[] = new int[50];
        
        for (int j = 0; j < 50; j++) {
            array[j] = 250 - j * 10;
        }
        
        for (int j = 0; j < 50; j++) {
            groupScale.getChildren().add(new Rectangle(10, 1));
            groupScale.getChildren().add(new Label(String.valueOf(array[j])));
        }
        
        for (int k = 0; k < 100; k++) {
            groupScale.getChildren().get(k).setLayoutX(0);
            groupScale.getChildren().get(k).setLayoutY(k * 5);
            
            groupScale.getChildren().get(k + 1).setLayoutX(20);
            groupScale.getChildren().get(k + 1).setLayoutY(k * 5 - 7);
            groupScale.getChildren().get(k + 1).setScaleY(0.6);
            groupScale.getChildren().get(k + 1).setScaleX(0.8);
            k++;
        }
        
        root.getChildren().addAll(c, r, play, stop, alertPane,  
                infoTable, finishC, groupScale);
        
        
        play.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(data.getP() != 0){
                finishC.setVisible(false);
                secToWater.setText("--");
                if(data.getDataChanged()){
                    transition.stop();
                    data.setDataChanged(false);
                }
                if(transition.getStatus() == STOPPED){
                    
                animationConstructor();
                SpeedController cont = new SpeedController(data, currentV,
                        maxVAir, maxVWater, transition, finishC, secToWater);
                
                }
                                   
                    transition.play();
                    stop.setText("Pause");
                }
                
            }
        ;
        });
        
            play.addEventFilter(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!data.getDataChanged() & data.getP() == 0) {
                    alertPane.setVisible(true);
                }
            }
        }
        );
        play.addEventFilter(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent
            ) {
                alertPane.setVisible(false);
                
            }
        }
        );
        stop.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent
            ) {
                if (transition.getStatus() == PAUSED) {
                    transition.stop();
                    stop.setText("Stpd");
                    stop.setPrefWidth(60);
                    
                } else {
                    transition.pause();
                    stop.setText("Stop");
                    stop.setPrefWidth(60);
                }
            }
        }
        );
        
    }
    
     public PathTransition getTransition(){
         return transition;
     }
    
    public Circle getCircle(){
        return c;
    }
    
}
