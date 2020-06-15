package main;

import java.util.InputMismatchException;

import org.bson.Document;

public class Driver {
	private String name;
	private String phoneNumber;
	private CarType carType;
	private boolean isBusy;
	private long cash;
	private double sumOfRatings;
	private long ratingCounts;
	
	public Driver(String name, String phoneNumber, CarType carType) {
		if(name != null && !name.trim().isEmpty() && name.length() <= 20)
			this.name = name;
		else
			throw new InputMismatchException();
		if(phoneNumber.matches("09[0-9]{9}"))
			this.phoneNumber = phoneNumber;
		else
			throw new InputMismatchException();
		if(carType != null)
			this.carType = carType;
		else
			throw new InputMismatchException();
		this.isBusy = false;
		this.cash = 0;
		this.sumOfRatings = 0;
		this.ratingCounts = 0;
	}
	
	public Driver(Document doc) {
		name = doc.getString("name");
		phoneNumber = doc.getString("phoneNumber");
		carType = CarType.getCarType(doc.getString("carTypeName"));
		isBusy = doc.getBoolean("isBusy", false);
		cash = doc.getLong("cash");
		sumOfRatings = doc.getDouble("sumOfRatings");
		ratingCounts = doc.getLong("ratingCounts");
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public Document generateDocument(){
		Document driverDocument = new Document();
		driverDocument.append("name", name);
		driverDocument.append("phoneNumber", phoneNumber);
		driverDocument.append("carTypeName", carType.name);
		driverDocument.append("isVIP", carType.isVIP());
		driverDocument.append("isBusy", isBusy);
		driverDocument.append("cash", cash);
		driverDocument.append("sumOfRatings", sumOfRatings);
		driverDocument.append("ratingCounts", ratingCounts);
		return driverDocument;
	}
	
	public void endTravel(Double rating) {
		isBusy = false;
		this.cash += 10000;
		this.sumOfRatings += rating;
		this.ratingCounts++;
	}

	public Document generateEndTravelUpdatedDocument(){
		Document document = new Document();
		document.append("isBusy", isBusy);
		document.append("cash", cash);
		document.append("sumOfRatings", sumOfRatings);
		document.append("ratingCounts",ratingCounts);
		return document;
	}

	private double getAverageRating() {
		if(ratingCounts == 0)
			return 0;
		return sumOfRatings/ratingCounts;
	}

	@Override
	public String toString() {
		return "Driver [name= " + name + ", phoneNumber= " + phoneNumber
				+ ", carType= " + carType.name + ", cash= "
				+ cash + ", averageRatings= " + getAverageRating() + "]";
	}

}