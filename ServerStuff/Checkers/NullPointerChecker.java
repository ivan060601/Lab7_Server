package ServerStuff.Checkers;

import CityStructure.City;
import CityStructure.Coordinates;
import CityStructure.Human;
import CityStructure.StandardOfLiving;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

public class NullPointerChecker implements Checker {
    Scanner scanner = new Scanner(System.in);
    String newLine = new String();
    static Logger logger = Logger.getLogger("NullCheckLogger");

    @Override
    public void checkEverything(City city, CheckParameter parameter){
        if (parameter == CheckParameter.WITH_ASKING) {
            if (city.getName() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city name is null. \n Enter the new name:");

                while (!passedValue) {
                    try {
                        if ((newLine = scanner.nextLine()) != "") {
                            city.setName(newLine);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one:");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with default value");
                        city.setName("City" + city.getId());
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("I dont feel so good");
                        System.exit(0);
                    }
                }
            }

            if (Float.valueOf(city.getArea()) == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city area is null. \n Enter the new area:");

                while (!passedValue) {
                    try {

                        float tempArea = Float.parseFloat(newLine = scanner.nextLine());

                        if (tempArea > 0) {
                            city.setArea(tempArea);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one:");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("This is not a number");
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with random value");
                        city.setArea((float) (Math.random() * 333) + 1f);
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("I dont feel so good");
                        System.exit(0);
                    }
                }
            }

            if (Integer.valueOf(city.getPopulation()) == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city population is null. \n Enter the new population:");

                while (!passedValue) {
                    try {

                        float tempPopulation = Integer.parseInt(newLine = scanner.nextLine());

                        if (tempPopulation > 0) {
                            city.setArea(tempPopulation);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one:");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("This is not a number");
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with random value");
                        city.setArea((int) (Math.random() * 2000000) + 1);
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("I dont feel so good");
                        System.exit(0);
                    }
                }
            }

            if (Long.valueOf(city.getCarCode()) == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city car-code is less than 0. \n Enter the new car-code:");

                while (!passedValue) {
                    try {

                        float tempCarCode = Long.parseLong(newLine = scanner.nextLine());

                        if (tempCarCode > 0) {
                            city.setArea(tempCarCode);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one:");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("This is not a number");
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with random value");
                        city.setArea((long) (Math.random() * 999) + 1L);
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("I dont feel so good");
                        System.exit(0);
                    }
                }
            }

            if (city.getCoordinates() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city coordinates are null. \n Enter the new coordinates:");

                city.setCoordinates(new Coordinates(0, 0));
                changeX(city);
                changeY(city);
            }

            if (Float.valueOf(city.getCoordinates().getX()) == null) {
                System.out.println("Looks like the city X-coordinate is null.");
                changeX(city);
            }

            if (Double.valueOf(city.getCoordinates().getY()) == null) {
                System.out.println("Looks like the city Y-coordinate is null.");
                changeY(city);
            }

            if (city.getEstablishmentDate() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the city establishment date is null. \n Enter the new date:");

                while (!passedValue) {
                    try {
                        city.setEstablishmentDate(LocalDate.parse(newLine = scanner.nextLine()));
                    } catch (NumberFormatException e) {
                        System.out.println("This is not a number.");
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with random value.");
                        city.setEstablishmentDate(LocalDate.of((int) Math.random() * 2020, (int) Math.random() * 11 + 1 , (int) Math.random() * 20 + 1));
                        passedValue = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("Wrong date format. Try another one:");
                    } catch (Exception e) {
                        System.out.println("I dont feel so good");
                        System.exit(0);
                    }
                }
            }
        }else {
            this.checkEverything(city);
        }
    }

    @Override
    public void checkEverything(City city) {
        if (city.getName() == null){
            logger.info("Name-field is null, it will be replaced with default value");

            city.setName("id" + city.getId());
        }

        if (city.getCoordinates() == null){
            logger.info("Coordinates-field is null, it will be replaced with default value");

            city.setCoordinates(new Coordinates(100f,100d));
        }

        if (city.getCoordinates().getX() == null){
            logger.info("XCoordinate-field is null, it will be replaced with default value");

            city.getCoordinates().setX(100f);
        }

        if (Double.valueOf(city.getCoordinates().getY()) == null){
            logger.info("YCoordinate-field is null, it will be replaced with default value");

            city.getCoordinates().setY(100d);
        }

        if (Double.valueOf(city.getArea()) == null){
            logger.info("Area-field is null, it will be replaced with default value");

            city.setArea(51f);
        }

        if (Integer.valueOf(city.getPopulation()) == null){
            logger.info("Population-field is null, it will be replaced with default value");

            city.setPopulation(100);
        }

        if (Long.valueOf(city.getMetersAboveSeaLevel()) == null){
            logger.info("MetersAboveSeaLevel-field is null, it will be replaced with default value");

            city.setMetersAboveSeaLevel(1337L);
        }

        if (city.getEstablishmentDate() == null){
            logger.info("EstablishmentDate-field is null, it will be replaced with default value");

            city.setEstablishmentDate(LocalDate.now());
        }

        if (Long.valueOf(city.getCarCode()) == null){
            logger.info("CarCode-field is null, it will be replaced with default value");

            city.setCarCode(1488L);
        }


        if (city.getStandardOfLiving() == null){
            logger.info("StandardOfLiving-field is null, it will be replaced with default value");
            city.setStandardOfLiving(StandardOfLiving.MEDIUM);
        }

        if (city.getGovernor() == null){
            logger.info("Governor-field is null, it will be replaced with default value");
            city.setGovernor(new Human(Date.valueOf("2020-10-10")));
        }
    }

    private void changeX(City city){
        boolean passedValue = false;
        System.out.println("Enter new X-coordinate:");

        while (!passedValue) {
            try {

                float tempX = Float.parseFloat(newLine = scanner.nextLine());

                if (tempX > 0) {
                    city.setArea(tempX);
                    passedValue = true;
                } else {
                    System.out.println("Entered value is inappropriate. Try another one:");
                }

            } catch (NumberFormatException e) {
                System.out.println("This is not a number");
            } catch (NoSuchElementException e) {
                System.out.println("End of input. Field will be replaced with random value");
                city.setArea((float) (Math.random() * 348) + 1f);
                passedValue = true;
            } catch (Exception e) {
                System.out.println("I dont feel so good");
                System.exit(0);
            }
        }
    }

    private void changeY(City city){
        boolean passedValue = false;
        System.out.println("Enter new Y-coordinate:");

        while (!passedValue) {
            try {

                float tempX = Float.parseFloat(newLine = scanner.nextLine());

                if (tempX > 0) {
                    city.setArea(tempX);
                    passedValue = true;
                } else {
                    System.out.println("Entered value is inappropriate. Try another one:");
                }

            } catch (NumberFormatException e) {
                System.out.println("This is not a number.");
            } catch (NoSuchElementException e) {
                System.out.println("End of input. Field will be replaced with random value.");
                city.setArea((float) (Math.random() * 348) + 1f);
                passedValue = true;
            } catch (Exception e) {
                System.out.println("I dont feel so good");
                System.exit(0);
            }
        }
    }
}
