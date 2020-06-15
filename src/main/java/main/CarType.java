package main;

import java.util.ArrayList;
import java.util.Arrays;

public enum CarType {
	BMV("BMV"),
	PRIDE("pride"),
	PEUGEOT_206("peugeot206"), 
	PEUGEOT_GLX("peugeotGLX"), 
	RANA("rana"), 
	CERATO("cerato"),
	LEXUS("lexus");
	
	String name;

	CarType(String name){
		this.name = name;
	}
	
	public boolean isVIP(){
		switch (this) {
		case BMV:
		case CERATO:
		case LEXUS:
			return true;
		default:
			return false;
		}
	}
	
	public static ArrayList<String> getCarTypeNames(){
		ArrayList<String> carTypeNames = new ArrayList<>();
		Arrays.asList(CarType.values()).stream().forEach(value -> carTypeNames.add(value.name));
		return carTypeNames;
	}
	
	public static CarType getCarType(String key){
		return Arrays.asList(CarType.values()).stream()
				.filter(value -> value.name.equals(key)).findFirst().get();
	}
}
