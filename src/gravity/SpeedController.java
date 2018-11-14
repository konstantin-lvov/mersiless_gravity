package gravity;

import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author fromchaos
 */
public class SpeedController implements Runnable {

    private Data data = new Data();
    private Label currentV = new Label();
    private Label maxVAir = new Label();
    private Label maxVWater = new Label();
    private Label finishC = new Label();
    private Label secToWater = new Label();
    private PathTransition trans = new PathTransition();
    private Thread go;
    private int tmpTransitionVol;
    private int tmpCurrentV = 0;
    private int tmpMaxVAir = 0;
    private int tmpMaxVWater = 0;
    private double tmpDBL;
    private long currentTime;
    private boolean oneTimeCond = true;

    SpeedController(Data d, Label cV, Label mA, Label mW, PathTransition pt, Label fC, Label sTW) {
        this.finishC = fC;
        this.secToWater = sTW;
        this.data = d;
        this.currentV = cV;
        this.maxVAir = mA;
        this.maxVWater = mW;
        this.trans = pt;
        go = new Thread(this);
        go.start();

        currentV.setText(String.valueOf(tmpCurrentV));
        maxVAir.setText(String.valueOf(tmpCurrentV));
        maxVWater.setText(String.valueOf(tmpCurrentV));
    }

    public void run() {
        try{
        trans.getNode().translateYProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable,
                    Object oldValue, Object newValue) {

                //текущее положение шара по Y
                tmpTransitionVol = ((int) (double) newValue) + 50;
                
                //если список всех скоростей содержит текущую координату
                if (data.getMap().containsKey(tmpTransitionVol)) {
                    tmpCurrentV = data.getV(tmpTransitionVol);
                    currentV.setText(String.valueOf(tmpCurrentV));
                    
                    if(tmpTransitionVol == 50.0){
                       currentV.setText("0"); 
                    }
                    
                    //Если над водой
                    if (tmpCurrentV > tmpMaxVAir & tmpTransitionVol < 250) {
                        if (tmpCurrentV == 0 | tmpCurrentV == 1) {
                            maxVAir.setText(String.valueOf((int) data.getRiverV()));
                            tmpMaxVAir = tmpCurrentV;
                        } else {
                            maxVAir.setText(String.valueOf(tmpCurrentV));
                            tmpMaxVAir = tmpCurrentV;
                        }
                    }
                    //Если под водой
                    if (tmpCurrentV >= tmpMaxVWater & tmpTransitionVol >= 250) {
                        
                        if (oneTimeCond){
                            secToWater.setText(String.valueOf(data.getSecToWater()));
                            oneTimeCond = false;
                        }
                            
                        if (tmpCurrentV == 0 | tmpCurrentV == 1) {
                            maxVWater.setText(String.valueOf((int) data.getRiverV()));
                            tmpMaxVWater = tmpCurrentV;
                        } else {
                            maxVWater.setText(String.valueOf(tmpCurrentV));
                            tmpMaxVWater = tmpCurrentV;
                        }
                    }
                }
                //если шар долетает до последней расчитаной координаты
                if ((int) data.getLastCoordY() == tmpTransitionVol) {
                    currentV.setText("0");
                    
                        //если перелет за экран и если его нет
                        if (trans.getNode().translateXProperty().get() + 150 > 400) {
                            if (data.getFinishCondition().equals(" Перелет! ")) {
                                finishC.setLayoutX(trans.getPath().getScene().getWidth()-150);
                            } else {
                                finishC.setLayoutX(trans.getNode().translateXProperty().get() - 100);
                            }
                        } else {
                            finishC.setLayoutX(trans.getNode().translateXProperty().get() + 150);
                        }

                        finishC.setLayoutY(trans.getNode().translateYProperty().get() + 70);
                        finishC.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE,
                                new CornerRadii(5), new Insets(0.0, 0.0, 0.0, 0.0))));
                        finishC.setText(data.getFinishCondition());
                        finishC.setVisible(true);
                       Thread.currentThread().interrupt();
                }
                
            }
        });
        } catch (Exception e){
            
        }

    }


}
