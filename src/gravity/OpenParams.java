package gravity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenParams {
    
    private Params pm;
    private File file;
    private Data data;
    private String tmpLine;
    private boolean wasItWarning = false;
    
    OpenParams(Params pm, File file, Data data) {
        this.pm = pm;
        this.file = file;
        this.data = data;
    }
    
    public void openIt() {
        //очищаем все поля на вкладке параметры
        pm.getName().clear();
        pm.getDensity().clear();
        pm.getVolume().clear();
        pm.getHspeed().clear();
        //удаляем из названия файла расширение и устанавливаем оставшееся как текст в поле Имя
        pm.setNameOk(true);
        char[] dist = new char[file.getName().length() - 4];//массив символов длинной название_файла минус 4 символа - ".txt"
        file.getName().getChars(0, file.getName().length() - 4, dist, 0);//получаем подстроку из названия файла в массив символов
        pm.getName().setText(String.valueOf(dist));
        
        String tmp;
        
        try {
            FileReader fr = new FileReader(file);
            Scanner scan = new Scanner(fr);
            while (scan.hasNext()) {
                tmpLine = scan.nextLine();
                
                if (tmpLine.equals("[плотность]")) {
                    try {
                        tmp = scan.nextLine();
                        Double.parseDouble(tmp);//Пробую парсить отсканированое значение, что бы вызвать исключение
                        pm.getDensity().setText(tmp);
                        pm.setDensOk(true);
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [плотность]");
                        pm.setDensOk(false);
                    }
                }
                
                if (tmpLine.equals("[объем]")) {
                    try {
                        tmp = scan.nextLine();
                        Double.parseDouble(tmp);
                        pm.getVolume().setText(tmp);
                        pm.setVolOk(true);
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [объем]");
                        pm.setVolOk(false);
                    }
                }
                
                if (tmpLine.equals("[горизонтальная_скорость]")) {
                    try {
                        tmp = scan.nextLine();
                        Double.parseDouble(tmp);
                        pm.getHspeed().setText(tmp);
                        pm.setHsOk(true);
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [горизонтальная_скорость]");
                        pm.setHsOk(false);
                    }
                }
                
                if (tmpLine.equals("[реверс]")) {
                    try {
                        if (scan.nextLine().equals("true")) {
                            pm.getArCheck().setSelected(true);
                            data.setAutoReverse(true);
                        } else {
                            pm.getArCheck().setSelected(false);
                            data.setAutoReverse(false);
                        }
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [реверс]");
                        pm.getArCheck().setSelected(false);
                        data.setAutoReverse(false);
                    }
                    
                }
                
                if (tmpLine.equals("[путь]")) {
                    try {
                        if (scan.nextLine().equals("true")) {
                            pm.getSpCheck().setSelected(true);
                            data.setShowPath(true);
                        } else {
                            pm.getSpCheck().setSelected(false);
                            data.setShowPath(false);
                        }
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [путь]");
                        pm.getSpCheck().setSelected(false);
                        data.setShowPath(false);
                    }
                }
                
                if (tmpLine.equals("[течение]")) {
                    try {
                        if (scan.nextLine().equals("true")) {
                            pm.getRcCheck().setSelected(true);
                            data.setRiverC(true);
                        } else {
                            pm.getRcCheck().setSelected(false);
                            data.setRiverC(false);
                        }
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [течение]");
                        pm.getRcCheck().setSelected(false);
                        data.setRiverC(false);
                    }
                }
                
                if (tmpLine.equals("[скорость_анимации]")) {
                    try {
                        tmp = scan.nextLine();
                        
                        if (tmp.equals("1")) {
                            pm.getGroup().getToggles().get(0).setSelected(true);
                        }
                        if (tmp.equals("2")) {
                            pm.getGroup().getToggles().get(1).setSelected(true);
                        }
                        if (tmp.equals("3")) {
                            pm.getGroup().getToggles().get(2).setSelected(true);
                        }
                        if (tmp.equals("4")) {
                            pm.getGroup().getToggles().get(3).setSelected(true);
                        }
                        
                        data.setDt(Integer.parseInt(tmp));
                    } catch (Exception e) {
                        wasItWarning = true;
                        System.out.println("Ошибка в файле с параметрами после строки [скорость_анимации]");
                        pm.getGroup().getToggles().get(0).setSelected(true);
                        data.setDt(1);
                    }
                }
                
            }
            scan.close();
            fr.close();
            if (wasItWarning) {
                pm.getWFO().setVisible(true);
            } else {
                pm.getWFO().setVisible(false);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OpenParams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
