package gravity;

import java.util.ArrayList;

public class CalPath {

    private Data data = new Data();
    private double threeNumsArray[] = new double[3];
    private double V;
    private double F;
    private Double lastCoordY = new Double(50);
    private Double lastCoordX = new Double(150);
    private final double acA = 181.9;
    private final double acW = 1024;
    private final double pcA = 1.293;
    private final double pcW = 1000;
    private double p;
    private double v;
    private double riverV;
    private double Hspeed;
    private ArrayList <Double> coordX = new ArrayList<Double>();
    private ArrayList <Double> coordY = new ArrayList<Double>();
    private double expectedV;
    private double currentV;
    
    CalPath(){
        
    }
    
    CalPath(Data data){
        this.data = data;
    }

    //метод вызывается перед созданием объекта класса
    //для связывания полей с задаными параметрами и уже создаными объектами других классов
    public void setAllVar() {

        this.riverV = data.getRiverV();
        p = data.getP();
        v = data.getV();
        Hspeed = data.getHspeed();

        for (byte b = 0; b < 3; b++) {
            threeNumsArray[b] = 0.0;
        }

    }

    //вычисляем координаты
    public void calcCoords() {

        //очищаем хранилища координат и присваеваем первые координаты
        coordX.clear();
        coordY.clear();
        lastCoordX = 150.0;
        lastCoordY = 50.0;
        coordX.add(lastCoordX);
        coordY.add(lastCoordY);

        while (canCondition(lastCoordY)) {
            expectedV = V + Math.abs((p * v * 9.81 - V * acA - pcA * v) / (p * v));
            if (lastCoordY + expectedV <= 250 | lastCoordY == 250.0) {
                F = p * v * 9.81 - airResistance(V * acA) - pcA * v;

                V = V + F / (p * v);

//                  Если предыдущая координата была ровно 250, значит было всплытие из под воды
//                  И тогда узнаем какая точка была по иkc, в точке 251 по игреку
                if (lastCoordY == 250.0) {

                    lastCoordX = findXPoint();
                    coordX.add(lastCoordX);
                    lastCoordY = 251.0;
                    coordY.add(lastCoordY);
                    V = 1;
                    speedMonitor();
                    continue;
                }
                speedMonitor();
                lastCoordX += Hspeed;
                coordX.add(lastCoordX);
                lastCoordY += V;
                coordY.add(lastCoordY);

            } else {
                if (V > 0 & lastCoordY <= 250) {
                    //координата расчитывается в процентах от пройденого пути по игреку
                    lastCoordX = lastCoordX + ((Hspeed / 100) * ((251 - lastCoordY) / (V / 100)));
                    coordX.add(lastCoordX);
                    lastCoordY = 251.0;
                    coordY.add(lastCoordY);
                    V = 0;
                    speedMonitor();
                }

                F = (p * v * 9.81) - Math.abs(V * acW) - (pcW * v * 9.81);

                V = V + F / (p * v);

//              Если следующая координата будет над водой, то узнаем какая будет точка по икс
//              если шар встанет на 250 по игреку
                if (lastCoordY + V <= 250.0) {

                    lastCoordX = findXPoint();
                    coordX.add(lastCoordX);
                    lastCoordY = 250.0;
                    coordY.add(lastCoordY);
                    V = 1;
                    speedMonitor();
                } else {//если нет, то записываем как обычно
                    speedMonitor();
                    lastCoordX += riverV;
                    coordX.add(lastCoordX);
                    lastCoordY += V;
                    coordY.add(lastCoordY);
                }
            }
        }

        data.setLastCoordY(lastCoordY);
        for (int i = 0; i < coordX.size(); i++) {
            System.out.printf("%d - x: %.2f y: %.2f\n", i, coordX.get(i), coordY.get(i));
        }
        System.out.println("===================================================");
        clearAllVar();

    }

    //метод принимает сопротивление воздуха в текущей точке расчета
    //и определяет превышает ли оно силу притяжения,
    //если превышает то возвращается сила притяжения, если нет то сопротивление
    public double airResistance(double resistance) {
        if (resistance > p * v * 9.81) {
            return p * v * 9.81;
        } else {
            return resistance;
        }
    }

    public void speedMonitor() {
        if (lastCoordY < 250) {
            data.addOneSec();
        }
        data.addV(lastCoordY.intValue(), Math.abs((int) V));
    }

//    метод для поиска условий прекращения основного цикла вычислений координат
    public boolean canCondition(double lastCoordY) {
        double sumTwoDiff = 0;
        double difference;

        threeNumsArray[0] = threeNumsArray[1];
        threeNumsArray[1] = threeNumsArray[2];
        threeNumsArray[2] = lastCoordY;

        for (byte b = 0; b < 2; b++) {
            difference = Math.abs(threeNumsArray[b] - threeNumsArray[b + 1]);
            sumTwoDiff += difference;
        }
        if (lastCoordX > 699) {
            data.setFinishCond(" Перелет! ");
            return false;
        }
        if (sumTwoDiff < 3) {
            data.setFinishCond(" Незначительные изменения по оси Y ");
            return false;
        }
        if (lastCoordY > 300) {

            data.setFinishCond(" Стабильное погружение ");

            return false;
        } else {
            return true;
        }
    }

//  Метод находит точку икс соответствующую точке 250 по игреку
    public double findXPoint() {

        double xStep = riverV / Math.abs(V);
        double passedXWay;
        if (lastCoordY > 250) {
            passedXWay = xStep * Math.abs(lastCoordY - 250);
        } else {
            passedXWay = xStep * Math.abs(lastCoordY - 251);
        }

        if (data.isRiverC()) {
            if (passedXWay <= 1) {
                return lastCoordX + 1;
            } else {
                return lastCoordX + passedXWay;
            }
        } else {
            return lastCoordX;
        }

    }

    public ArrayList getCoordX() {
        return coordX;
    }

    public ArrayList getCoordY() {
        return coordY;
    }
    
    public void clearAllVar(){
    V = 0;
    F = 0;
    lastCoordY = 50.0;
    lastCoordX = 150.0;
    p = 0;
    v = 0;
    riverV = 0;
    Hspeed = 0;
    expectedV = 0;
    currentV = 0;
    }

}//
