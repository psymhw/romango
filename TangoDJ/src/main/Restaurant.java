package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Restaurant {
	 
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty city;
    private final SimpleStringProperty name;

    public Restaurant(int id, String city, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.city = new SimpleStringProperty(city);
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int idx) {
        id.set(idx);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String fName) {
        city.set(fName);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }
}
