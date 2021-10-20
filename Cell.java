public class Cell {
	private String val;
	private int bombProximityNum;  //  number of bombs in its surrounding cells
	private boolean isRevealed = false;  // If true then value of cell is revealed
	
	public Cell(String val) {
		this.val = val;
		this.bombProximityNum = 0;
	}
	
	public void setVal(String val) {
		this.val = val;
	}
	
	public String getVal() {
		if (isRevealed) {
			if (bombProximityNum > 0) {
				return Integer.toString(bombProximityNum);
			} else {
				return val;
			}
 
		} else { 
			return "?"; 
		}
	}
	
	public void toggleRevealed() {
		isRevealed = !isRevealed;
	}
	
	public void increment() {
		if (!val.equals("*")) {
			bombProximityNum++;
		}		
	}
	
	public void resetBombProxNum() {
		this.bombProximityNum = 0;
	}
}
