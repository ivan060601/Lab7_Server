package CityStructure;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentSkipListSet;

public class CityTree extends ConcurrentSkipListSet<City> {
    public LocalDateTime authDateTime;

    public void setAuthDateTime(LocalDateTime authDateTime) {
        this.authDateTime = authDateTime;
    }

    public LocalDateTime getAuthDateTime(){
        return this.authDateTime;
    }
}
