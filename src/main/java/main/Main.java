package main;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Scanner;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class Main {

	static final String _ID = "_id";
	
	private static Scanner scanner;
	private static MongoCollection<Document> collection;
	private static boolean isOpen;
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		isOpen = true;
		MongoClient client = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = client.getDatabase("TravelAgency");
		collection = database.getCollection("drivers");
		
		System.out.println("WELCOME TO TRAVEL AGENCY");
		System.out.println("/help to start");

		while(isOpen){
			String input = scanner.next();
			rout(input);
		}
	}

	private static void rout(String input) {
		switch (input) {
			case "/help":
				help();
				break;
			case "/add":
				addDriver();
				break;
			case "/remove":
				removeDriver();
				break;
			case "/list":
				showAvailableDrivers();
				break;
			case "/request":
				requestToTravel();
				break;
			case "/end":
				endTravel();
				break;
			case "/exit":
				exit();
				break;
			default:
				wrongInput();
				break;
		}
	}

	private static void help() {
		String help = "click /add to add new driver" + "\n" + 
				"click /remove to remove driver" + "\n" +
				"click /list to show available drivers" + "\n" +
				"click /request to request for travel" + "\n" +
				"click /end when end your travel" + "\n" +
				"click /exit to exit from application";
		System.out.println(help);
	}
	
	private static void addDriver() {
		System.out.println("Enter name:");
		String name = scanner.next();
		System.out.println("Enter phone number:");
		String phoneNumber = scanner.next();
		System.out.println("Chose car from below list:");
		ArrayList<String> carTypeNames = CarType.getCarTypeNames();
		int size = carTypeNames.size();
		for (int i = 0; i < size; i++) {
			String carTypeName = carTypeNames.get(i);
			System.out.print(carTypeName);
			if(i < size - 1)
				System.out.print(" , ");
		}
		System.out.println();
		String carTypeName = scanner.next();
		try{
			Driver driver = new Driver(name, phoneNumber, CarType.getCarType(carTypeName));
			BsonValue driverId = collection.insertOne(driver.generateDocument()).getInsertedId();
			System.out.println("It's your id: " + driverId.asObjectId().getValue());
		}catch(Exception e){
			System.err.println("Your input data is invalid!");
		}
	}

	private static void removeDriver() {
		System.out.println("Enter driver id:");
		String id = scanner.next();
		try{
			ObjectId objectId = new ObjectId(id);
			collection.updateOne(eq(_ID, objectId), new Document("$set", new Document("cash", 0)));
			DeleteResult deleteResult = collection.deleteOne(new Document(_ID, objectId));
			if(deleteResult.getDeletedCount() <= 0)
				System.err.println("No driver found by this id.");
			else
				System.out.println("Driver by this id was settled and deleted.");
		}catch(Exception e){
			System.err.println("No driver found by this id.");
		}
	}

	private static boolean isJustVIP() {
		System.out.println("Do you want just VIP drivers? Pleas type yes or no.");
		String input = scanner.next();
		if("yes".equals(input))
			return true;
		if("no".equals(input))
			return false;
		return isJustVIP();
	}

	private static void showAvailableDrivers(){
		FindIterable<Document> documents;
		boolean justVIP = isJustVIP();
		if(justVIP){
			documents = collection.find(and(eq("isBusy", false), eq("isVIP", true)));
		}else{
			documents = collection.find(eq("isBusy", false));
		}
		if(documents == null || documents.first() == null){
			String vip = justVIP ? " VIP " : " ";
			System.out.println("Sorry! No" + vip + "driver found yet.");
		}else{
			for (Document document : documents) {
				Driver driver = new Driver(document);
				System.out.println(driver.toString());
			}
		}
	}
	
	private static void requestToTravel(){
		boolean justVIP = isJustVIP();
		Document document;
		if(justVIP){
			document = collection.find(and(eq("isBusy", false), eq("isVIP", true))).first();
		}else{
			document = collection.find(eq("isBusy", false)).first();
		}
		if(document == null){
			String vip = justVIP ? " VIP " : " ";
			System.out.println("Sorry! No" + vip + "driver found yet.");
		}else{
			collection.updateOne(eq(_ID, document.get(_ID)), new Document("$set", new Document("isBusy", true)));
			Driver driver = new Driver(document);
			driver.setBusy(true);
			System.out.println(driver.toString());
		}
	}
	
	private static void endTravel(){
		System.out.println("How many points do you give to the driver from 0 to 5?");
		Double rating = 0.0;
		try{
			rating = Double.parseDouble(scanner.next());
		}catch(Exception e){
			System.err.println("Your point is invalid.");
		}
		Document document = collection.find(eq("isBusy", true)).first();
		
		Driver driver = new Driver(document);
		driver.endTravel(rating);
		
		collection.updateOne(eq(_ID, document.get(_ID)), new Document("$set", driver.generateEndTravelUpdatedDocument()));
		System.out.println("We hope you have a good travel.");
	}

	private static void exit() {
		System.out.println("Thank you for your choice.");
		isOpen = false;
	}

	private static void wrongInput() {
		System.out.println("Wrong Input! type /help");
	}

}
