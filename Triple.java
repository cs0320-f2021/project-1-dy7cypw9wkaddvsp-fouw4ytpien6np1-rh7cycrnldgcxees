package edu.brown.cs.student.main.KDT;

public class Triple <X,Y,Z> {
    private final X xVal;
    private final Y yVal;
    private final Z zVal;

    public Triple(X x, Y y, Z z){
        this.xVal = x;
        this.yVal = y;
        this.zVal = z;
    }
    public X getX(){return this.xVal;}
    public Y getY(){return this.yVal;}
    public Z getZ(){return this.zVal;}
}
