package framework;

public class MapIndex {
	
	private int x = -1;
	private int y = -1;
	private int i = -1;
	
	public MapIndex(){
	}
	
	public MapIndex(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapIndex other = (MapIndex) obj;
		if (i != other.i)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}




	
	

}
