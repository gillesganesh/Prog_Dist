package com.example.rentalService.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RentalWebService {

    Logger logger = LoggerFactory.getLogger(RentalWebService.class);

    private List<Car> cars = new ArrayList<>();

    public RentalWebService() {
        // Initialiser quelques voitures pour la démonstration
        cars.add(new Car("11AA22", "Ferrari", 100));
        cars.add(new Car("33BB44", "Mercedes", 50));
        cars.add(new Car("55CC66", "BMW", 80));
    }

    // Endpoint pour afficher un message de test
    @GetMapping("/bonjour")
    public String disBonjour() {
        return "Bonjour !";
    }

    // Endpoint pour obtenir la liste des voitures disponibles
    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    public List<Car> listOfCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (!car.isRented()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    // Endpoint pour obtenir les détails d'une voiture spécifique
    @GetMapping("/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Car aCar(@PathVariable("plateNumber") String plateNumber) throws Exception {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                return car;
            }
        }
        throw new Exception("Voiture introuvable !");
    }

    // Endpoint pour louer ou rendre une voiture
    @PutMapping(value = "/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void rentOrGetBack(
            @PathVariable("plateNumber") String plateNumber,
            @RequestParam(value = "rent", required = true) boolean rent,
            @RequestBody(required = false) Dates dates) throws Exception {

        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                if (rent && !car.isRented()) {
                    car.setRented(true);
                    logger.info("Voiture louée : " + car + ", Dates : " + dates);
                } else if (!rent && car.isRented()) {
                    car.setRented(false);
                    logger.info("Voiture rendue : " + car);
                } else {
                    throw new Exception("Action non valide pour la voiture : " + plateNumber);
                }
                return;
            }
        }
        throw new Exception("Voiture introuvable !");
    }
}
