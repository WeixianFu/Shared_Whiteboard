// Student Name: Weixian Fu
// Student ID: 1183945

package src;
import java.io.Serializable;

public class Shape implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int x3 = 0;
	private int y3 = 0;
	private int R;
	private int G;
	private int B;
	private String txt;
	private String username;
	private boolean fill;
	
	public Shape(String type, int x1, int x2, int y1, int y2, int R, int G, int B, String username){
		this.type = type;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.R = R;
		this.G = G;
		this.B = B;
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX1() {
		return this.x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return this.y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return this.x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return this.y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public int getX3() {
		return this.x3;
	}

	public void setX3(int x3) {
		this.x3 = x3;
	}
	
	public int getY3() {
		return this.y3;
	}

	public void setY3(int y3) {
		this.y3 = y3;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
}
