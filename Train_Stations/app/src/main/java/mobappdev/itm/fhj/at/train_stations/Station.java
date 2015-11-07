package mobappdev.itm.fhj.at.train_stations;

/**
 * Created by Viktoria on 06.11.2015.
 */
public class Station {
    /**
     * Created by Viktoria on 04.11.2015.
     */

        private String name;
        private int id;
        private double lon,lat;

        public Station(String name, int id, double lon, double lat){
            this.name=name;
            this.id=id;
            this.lon=lon;
            this.lat=lat;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }

        public void setName(String name) {
            this.name = name;
        }



}
