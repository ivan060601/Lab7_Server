package CityStructure;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Класс, опысывающий объекты City для коллекции
 */
public class City implements Comparable<City>, Serializable {
    final static long serialVersionUID = 15L;

    private static long previousID = 0;

    private long id; //А Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //А Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float area; //Значение поля должно быть больше 0
    private int population; //Значение поля должно быть больше 0
    private long metersAboveSeaLevel;
    private LocalDate establishmentDate;
    private long carCode; //Значение поля должно быть больше 0, Максимальное значение поля: 1000
    private StandardOfLiving standardOfLiving; //Поле может быть null
    private Human governor; //Поле может быть null
    private transient String owner;

    /**
     * Метод для задания всех полей объекта
     * @param name имя
     * @param coordinates координаты
     * @param area зона
     * @param population население
     * @param metersAboveSeaLevel метров над уровнем моря
     * @param establishmentDate дата основания
     * @param carCode код региона
     * @param standardOfLiving уровень жизни
     * @param governor градоначальник
     */
    public void setCity(long id, String name, Coordinates coordinates, LocalDateTime creationDate, float area, int population, long metersAboveSeaLevel, LocalDate establishmentDate, long carCode, StandardOfLiving standardOfLiving, Human governor, String owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.establishmentDate = establishmentDate;
        this.carCode = carCode;
        this.standardOfLiving = standardOfLiving;
        this.governor = governor;
        this.owner = owner;
    }

    public City(){
        this.setId();
        this.setCreationDate();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public float getArea() {
        return area;
    }

    public int getPopulation() {
        return population;
    }

    public long getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    public long getCarCode() {
        return carCode;
    }

    public StandardOfLiving getStandardOfLiving() {
        return standardOfLiving;
    }

    public Human getGovernor() {
        return governor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setMetersAboveSeaLevel(long metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public void setEstablishmentDate(LocalDate establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public void setCarCode(long carCode) {
        this.carCode = carCode;
    }

    public void setStandardOfLiving(StandardOfLiving standardOfLiving) {
        this.standardOfLiving = standardOfLiving;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    public void setCreationDate(){
        this.creationDate = LocalDateTime.now();
    }

    public void setId() {
        long id = System.currentTimeMillis() % 10000000000L;
        if ( id <= previousID ) {
            id = (previousID + 1) % 10000000000L;
        }
        previousID=id;
        this.id = id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @param city объект класса City
     * @return результат сравнения
     */
    @Override
    public int compareTo(City city) {
        int diff = 0;
        /*
        if(this.id - city.getId() > 0){
            diff++;
        }else if (this.id - city.getId() < 0){
            diff--;
        }
        */
        diff += String.valueOf(this.getName()).compareTo(String.valueOf(city.getName()));
        /*
        diff += String.valueOf(this.getStandardOfLiving()).compareTo(String.valueOf(city.getStandardOfLiving()));
        diff += String.valueOf(this.getEstablishmentDate()).compareTo(String.valueOf(city.getEstablishmentDate()));
        diff += Float.valueOf(this.getArea()).compareTo(Float.valueOf(city.getArea()));
        diff += Long.valueOf(this.getMetersAboveSeaLevel()).compareTo(Long.valueOf(city.getMetersAboveSeaLevel()));
        diff += Integer.valueOf(this.getPopulation()).compareTo(Integer.valueOf(city.getPopulation()));
        diff += Long.valueOf(this.getCarCode()).compareTo(Long.valueOf(city.getCarCode()));
        diff += Human.valueOf(this.getGovernor()).compareTo(Human.valueOf(city.getGovernor()));
        diff += String.valueOf(this.getCreationDate()).compareTo(String.valueOf(city.getCreationDate()));
        diff += Coordinates.valueOf(this.getCoordinates()).compareTo(Coordinates.valueOf(city.getCoordinates()));
        */
        return diff;
    }

    /**
     * @return поток вывода об элементе класса City
     */
    public String toString(){
        return "City ID: " + this.getId() + "\n"
                + "City name: " + this.getName() + "\n"
                + "Standard of living: " + this.getStandardOfLiving() + "\n"
                + "Coordinates: \n" + "\t x: " + this.getCoordinates().getX()
                + "\n \t y: " + this.getCoordinates().getY() + "\n"
                + "Governor: " + Human.valueOf(this.getGovernor());
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

