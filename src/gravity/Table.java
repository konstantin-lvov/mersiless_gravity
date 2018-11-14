package gravity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author fromchaos
 */
public class Table {

    TableView table = new TableView();
    CalPath cp = new CalPath();
    Group tableTabGroup;

    public void addTableWindow(Group tableTabGroup, CalPath cp) {

        this.tableTabGroup = tableTabGroup;
        this.cp = cp;
        table.setPrefSize(400, 450);

        TableColumn xCol = new TableColumn("Точки оси Х");
        xCol.setCellValueFactory(new PropertyValueFactory<Point2D, Double>("x"));
        xCol.setPrefWidth(200);

        TableColumn yCol = new TableColumn("Точки оси У");
        yCol.setCellValueFactory(new PropertyValueFactory<Point2D, Double>("y"));
        yCol.setPrefWidth(200);

        table.getColumns().addAll(xCol, yCol);
        this.tableTabGroup.getChildren().add(table);

    }

    public void refreshTable() {
        
        ObservableList obsr = FXCollections.observableArrayList();
        
        for (int i = 0; i < cp.getCoordX().size(); i++) {
            obsr.add(new Point2D((Double) cp.getCoordX().get(i),
                    (Double) cp.getCoordY().get(i)));
        }
        
        table.setItems(obsr);
    }

    public Group getTTG() {
        return tableTabGroup;
    }

}
