package main;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
	
	public static List<String> getCarTypeNames(){
		return Arrays.stream(CarType.values()).map(value -> value.name).collect(Collectors.toList());
	}
	
	public static CarType getCarType(String key) throws NoSuchElementException{
		return Arrays.stream(CarType.values())
				.filter(value -> value.name.equals(key)).findFirst().get();
	}
}
