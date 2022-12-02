package giaoDien;

public class OCo {
	private int x,y;
	public OCo(){
		x=y=0;
	}
	public OCo(int x,int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean isEqual(OCo oco) {
		if (oco.getX() != x || oco.getY() != y )return false;
		return true;
	}
}
