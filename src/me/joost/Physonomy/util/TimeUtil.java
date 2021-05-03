package me.joost.Physonomy.util;

public class TimeUtil {

	public static int StringToTime(String input) {
		int total = 0;
		String[] splitted = input.split(" ");
		for(String f : splitted) {
			int multiplyBy = 1;
			if(f.toLowerCase().endsWith("d")) { multiplyBy = 86400; }
			if(f.toLowerCase().endsWith("h")) { multiplyBy = 3600; }
			
			String am = f.substring(0, f.length()-1);
			int amount = Integer.parseInt(am);
			
			if(amount < 0) {
				amount = amount * multiplyBy;
				total += amount;
			}
		}
		return total;
	}
}
