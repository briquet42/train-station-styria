package mobappdev.itm.fhj.at.train_stations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktoria on 04.11.2015.
 */
public class ListStations {
    List<Station> stations;
    private Station sKap,sKapF,sMarin,sLeob;

    public ListStations(){
        stations=new ArrayList<Station>();
        sKap=new Station("Kapfenberg",8100031,47.444727,15.300195);
        sKapF=new Station("Kapfenberg Fachhochschule",8101032,47.454169, 15.330395);
        sMarin=new Station("Marein-St.Lorenzen", 8101243,47.474122,15.373639);
        sLeob=new Station("Leoben",8100070,47.386465,15.090009);

        addStations();
    }

    public void addStations() {
        stations.add(sKap);
        stations.add(sKapF);
        stations.add(sMarin);
        stations.add(sLeob);
    }
}
