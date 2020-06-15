package main;

import java.util.ArrayList;

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
		for (CarType carType : CarType.values()) {
			carTypeNames.add(carType.name);
		}
		return carTypeNames;
	}
	
	public static CarType getCarType(String key){
		if(BMV.name.equals(key))
			return BMV;
		if(PRIDE.name.equals(key))
			return PRIDE;
		if(PEUGEOT_206.name.equals(key))
			return PEUGEOT_206;
		if(	PEUGEOT_GLX.name.equals(key))
			return 	PEUGEOT_GLX;
		if(RANA.name.equals(key))
			return RANA;
		if(CERATO.name.equals(key))
			return CERATO;
		if(LEXUS.name.equals(key))
			return LEXUS;
		
		return null;
	}
}
