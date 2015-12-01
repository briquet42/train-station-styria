package mobappdev.itm.fhj.at.train_stations.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktoria on 04.11.2015.
 */
public class ListStations {
    public List<Station> stations;
    Station sKap,sKapF, sMarein,sLeob,sBruck, sAller, sNikl, sKin, sUnz, sGraz;

    public ListStations(){
        stations=new ArrayList<Station>();
        sKap=new Station("Kapfenberg",8100031,47.445351, 15.292030);
        sMarein =new Station("St. Marein im Mürztal", 8101243,47.474122,15.373639);
        sLeob=new Station("Leoben",8100070,47.386465,15.090009);
        sBruck=new Station("Bruck an der Mur",8100032,47.410051, 15.269105);
        sAller=new Station("Allerheiligen im Mürztal",8100613,47.480690, 15.406284);
        sNikl=new Station("Niklasdorf",8101275 ,47.394298, 15.155502);
        sKin=new Station("Kindberg",8100030 ,47.501363, 15.448393);
    //    sUnz=new Station("Unzmarkt", 8100074,47.198314, 14.442498); //Koordinaten
        sGraz=new Station("Graz",8100173,47.073182, 15.417046);
        sKapF=new Station("Kapfenberg Fachhochschule",8101032,47.454169, 15.330395);


        addStations();
    }

    public void addStations() {
        stations.add(sKap);
        stations.add(sKapF);
        stations.add(sMarein);
        stations.add(sLeob);
        stations.add(sBruck);
        stations.add(sAller);
        stations.add(sNikl);
        stations.add(sKin);
      //  stations.add(sUnz);
        stations.add(sGraz);
    }
}
