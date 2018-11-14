package gravity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class SaveParams {

    Params pm = new Params();

    SaveParams(Params pm) {
        this.pm = pm;
    }
    
    public void saveIt(){
        File file = new File(System.getProperty("user.dir"), pm.getName().getText() + ".txt");
        try {
            if(!file.createNewFile()){
             Alert alert = new Alert(AlertType.WARNING);
             alert.setTitle("Обратите внимание");
             alert.setContentText("Файл " + file.getName() + " перезаписан");
             alert.showAndWait();
            }
            FileWriter fr = new FileWriter(file);
            
            fr.append("[плотность]\n");
            fr.append(pm.getDensity().getText() + "\n");
            
            fr.append("[объем]\n");
            fr.append(pm.getVolume().getText() + "\n");
            
            fr.append("[горизонтальная_скорость]\n");
            fr.append(pm.getHspeed().getText() + "\n");
            
            fr.append("[реверс]\n");
            fr.append(String.valueOf(pm.getArCheck().isSelected()) + "\n");
            
            fr.append("[путь]\n");
            fr.append(String.valueOf(pm.getSpCheck().isSelected()) + "\n");
            
            fr.append("[течение]\n");
            fr.append(String.valueOf(pm.getRcCheck().isSelected()) + "\n");
            
            fr.append("[скорость_анимации]\n");
            fr.append(pm.getGroup().getSelectedToggle().getUserData().toString() + "\n");
            
            fr.flush();
            fr.close();
            
        } catch (IOException ex) {
            Logger.getLogger(SaveParams.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
