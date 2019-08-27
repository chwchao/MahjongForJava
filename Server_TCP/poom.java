package final_project;



public class poom {
	
	protected String saypoom;
	
	// 判斷有沒有碰，如果有碰在主程式吃牌;
	public poom(String thrown,card[] players) {
		int count = 0;
		for (int i=0;i<16;i++) {
			if (thrown.equals(players[i])) {
				count += 1;}
		}
		if (count == 2){
			this.saypoom = "碰";}
		else {
			this.saypoom = "不碰";
		}
	}
	public String get_poom() {
		return saypoom;
	}
}
