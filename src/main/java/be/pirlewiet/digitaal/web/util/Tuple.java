package be.pirlewiet.digitaal.web.util;

public class Tuple<X, Y> {
	
	protected X x;
	protected Y y;
	
	public Tuple(X x,Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return x;
	}
	public void setX(X x) {
		this.x = x;
	}
	public Y getY() {
		return y;
	}
	public void setY(Y y) {
		this.y = y;
	}
	
	public static<X,Y> Tuple<X,Y> tuple(X x, Y y){
		return new Tuple<X,Y>( x, y);
	}

}
