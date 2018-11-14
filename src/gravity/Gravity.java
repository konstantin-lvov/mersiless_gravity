package gravity;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Gravity extends Application {

    private Data data = new Data();
    private CalPath cp = new CalPath(data);
    private Params pm = new Params();
    private Table tbl = new Table();
    private Graph grph;
    private Group groupScale = new Group();
    private Tab paramTab = new Tab("Параметры");
    private Group paramTabGroup = new Group();
    private Tab tableTab = new Tab("Таблица");
    private Group tableTabGroup = new Group();
    private Tab graphTab = new Tab("Анимация");
    private Group graphTabGroup = new Group();
    private Label finishC = new Label();

    @Override
    public void start(final Stage stage) throws Exception {

        VBox root = new VBox();

        MenuBar menu = new MenuBar();

        Menu fileMenu = new Menu("Файл");
        MenuItem openParamMI = new MenuItem("Загрузить параметры");
        MenuItem saveParamMI = new MenuItem("Сохранить параметры");
        fileMenu.getItems().addAll(openParamMI, saveParamMI);

        Menu resultMenu = new Menu("Результат");
        MenuItem saveTableMI = new MenuItem("Сохранить таблицу");
        MenuItem printTableMI = new MenuItem("Распечатать таблицу");
        resultMenu.getItems().addAll(saveTableMI, printTableMI);

        Menu helpMenu = new Menu("Справка");
        MenuItem helpMI = new MenuItem("Помощь");
        MenuItem aboutMI = new MenuItem("О программе");
        helpMenu.getItems().addAll(helpMI, aboutMI);

        menu.getMenus().addAll(fileMenu, resultMenu, helpMenu);

        Separator sep = new Separator();

        //////////////////////////////
        //Создаю панель со вкладками//
        //////////////////////////////
        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(700, 450);
        tabPane.setSide(Side.LEFT);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        pm.assignData(data);
        pm.addParamsWindow(paramTabGroup, cp, tbl);
        paramTab.setContent(paramTabGroup);
        
        grph = new Graph(data, graphTabGroup, cp);
        grph.addGraphWindow();
        graphTab.setContent(graphTabGroup);

        tbl.addTableWindow(tableTabGroup, cp);
        tableTab.setContent(tableTabGroup);

        

        tabPane.getTabs().addAll(paramTab, tableTab, graphTab);

        root.getChildren().addAll(menu, sep, tabPane);

        Scene scene = new Scene(root, 700, 482);
        stage.setResizable(false);
        stage.setTitle("Merciless gravity");
        stage.setScene(scene);
        stage.show();

        saveTableMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                //Ищем среди файлов текущей директории уже сохраненные таблицы, если находим то считаем
                grph.getTransition().pause();
                File dir = new File(System.getProperty("user.dir"));
                String [] fileAndDirList;
                fileAndDirList = dir.list();
                int howMuchTable = 0;
                Pattern tablePat = Pattern.compile("^table[0-9]+[1-9]?\\.png$");
                
                for(int i = 0; i < fileAndDirList.length; i++){
                    Matcher m = tablePat.matcher(fileAndDirList [i]);
                    if(m.matches()){
                        howMuchTable++;
                    }
                }
                //формируем название новой таблицы
                String fileName = "table" + String.valueOf(howMuchTable) + ".png";

                WritableImage image = tbl.getTTG().snapshot(new SnapshotParameters(), null);

                File file = new File(fileName);

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    grph.getTransition().play();
                } catch (IOException e) {

                }
            }
        });

        printTableMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                try {
                    Printer printer = Printer.getDefaultPrinter();
                    PageLayout pageLayout = printer.createPageLayout(Paper.A4,
                            PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
                    double scaleX = pageLayout.getPrintableWidth()
                            / tableTabGroup.getBoundsInParent().getWidth();
                    double scaleY = pageLayout.getPrintableHeight()
                            / tableTabGroup.getBoundsInParent().getHeight();
                    tableTabGroup.getTransforms().add(new Scale(scaleX, scaleY));
                    PrinterJob pJob = PrinterJob.createPrinterJob();
                    if (pJob != null) {
                        boolean success = pJob.printPage(tbl.getTTG());

                        if (success) {
                            pJob.endJob();
                        }
                    }
                } catch (NullPointerException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setContentText("Принтер не обнаружен...");
                    alert.showAndWait();

                }
            }
        });

        openParamMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                try {
                    FileChooser fChooser = new FileChooser();
                    fChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                    fChooser.setTitle("Выбреите файл с параметрами");
                    fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.txt", "*.txt"));
                    File file = fChooser.showOpenDialog(stage);
                    OpenParams oParams = new OpenParams(pm, file, data);
                    oParams.openIt();
                } catch (NullPointerException e) {
                    System.out.println("Файл не выбран...");
                }
            }
        });

        saveParamMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                if (pm.checkConditions()) {
                    SaveParams sp = new SaveParams(pm);
                    sp.saveIt();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ошибочка");
                    alert.setContentText("Введены не все параметры!");
                    alert.showAndWait();
                }
            }
        });

        aboutMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                System.out.println("About");
                Alert alert = new Alert(AlertType.INFORMATION);

                alert.setTitle("О программе");
                alert.setContentText("Программа Merciless Gravity v 1\n"
                        + "Предназначена для визуализации\nпадения шара в воду.\n"
                        + "Автор Львов К.В.");
                alert.showAndWait();

            }
        });

        helpMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
//                использую стороннюю библиотеку BrowserLauncher т.к. awt.desktop работает не во всех ОС
                try {
                    BrowserLauncher launcher = new BrowserLauncher();
                    launcher.openURLinBrowser("http://lvovc.000webhostapp.com/MGH/MGhelp.html");
                } catch (BrowserLaunchingInitializingException ex) {
                    Logger.getLogger(Gravity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedOperatingSystemException ex) {
                    Logger.getLogger(Gravity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public static void main(final String[] arguments) {
        Application.launch(arguments);

    }
}
