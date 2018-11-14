package gravity;

import java.util.HashMap;
import java.util.Map;

public class Data {

    private double p = 0;
    private double v;
    private int dt = 1;
    private double Hspeed;
    private double riverV;
    private boolean autoReverse = false;
    private boolean showPath = false;
    private boolean riverC = false;
    private double lastCoordY;
    private double currentV = 0;
    private double maxVAir = 0;
    private double maxVWater = 0;
    private Map speedList = new HashMap<Integer, Integer>();
    private String finishCondition;
    private int secToWater;
    private boolean dataChanged = false;
    
    public void setDataChanged(boolean b){
        dataChanged = b;
    }
    
    public boolean getDataChanged(){
        return dataChanged;
    }

    public void addOneSec() {
        secToWater++;

    }

    public int getSecToWater() {
        return secToWater;
    }

    public void clearSecToWater() {
        secToWater = 0;
    }

    public void setFinishCond(String condition) {
        finishCondition = condition;
    }

    public String getFinishCondition() {
        return finishCondition;
    }

    public Map getMap() {
        return speedList;
    }

    public void clearMap() {
        speedList.clear();
    }

    public void addV(int key, int value) {
        try {
            speedList.put((Integer) key, (Integer) value);
        } catch (Exception e) {

        }
    }

    public int getV(int key) {
        try {
            return (int) speedList.get((Integer) key);
        } catch (Exception e) {
            return 1;
        }
    }

    public void setLastCoordY(double last) {
        this.lastCoordY = last;
    }

    public double getLastCoordY() {
        return lastCoordY;
    }

    public double getRiverV() {
        return riverV;
    }

    public double getcurrentV() {
        return currentV;
    }

    public void setCurrentV(double V) {
        this.currentV = V;
    }

    public double getmaxVAir() {
        return maxVAir;
    }

    public void setmaxVAir(double maxVAir) {
        this.maxVAir = maxVAir;
    }

    public double getmaxVWater() {
        return maxVWater;
    }

    public void setmaxVWater(double maxVWater) {
        this.maxVWater = maxVWater;
    }

    /**
     * @return the p
     */
    public double getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(double p) {
        this.p = p;
    }

    /**
     * @return the v
     */
    public double getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(double v) {
        this.v = v;
    }

    /**
     * @return the dt
     */
    public int getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(int dt) {
        this.dt = dt;
    }

    /**
     * @return the Hspeed
     */
    public double getHspeed() {
        return Hspeed;
    }

    /**
     * @param Hspeed the Hspeed to set
     */
    public void setHspeed(double Hspeed) {
        this.Hspeed = Hspeed;
    }

    /**
     * @return the autoReverse
     */
    public boolean isAutoReverse() {
        return autoReverse;
    }

    /**
     * @param autoReverse the autoReverse to set
     */
    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    /**
     * @return the showPath
     */
    public boolean isShowPath() {
        return showPath;
    }

    /**
     * @param showPath the showPath to set
     */
    public void setShowPath(boolean showPath) {
        this.showPath = showPath;
    }

    public boolean isRiverC() {
        return riverC;
    }

    public void setRiverC(boolean riverC) {
        this.riverC = riverC;

        if (this.riverC == false) {
            riverV = 0;
        } else {
            riverV = 4 + Math.random() * 10;
        }
    }

}
