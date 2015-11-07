package mobappdev.itm.fhj.at.train_stations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktoria on 04.11.2015.
 */
public class ListStations {
    List<Station> stations;
    private Station sKap,sKapF, sMarein,sLeob,sBruck, sAller, sNikl, sKin, sUnz, sGraz;

    public ListStations(){
        stations=new ArrayList<Station>();
        sKap=new Station("Kapfenberg",8100031,47.445351, 15.292030);
        sKapF=new Station("Kapfenberg Fachhochschule",8101032,47.454169, 15.330395);
        sMarein =new Station("Marein-St.Lorenzen", 8101243,47.474122,15.373639);
        sLeob=new Station("Leoben",8100070,47.386465,15.090009);
        sBruck=new Station("Bruck",8100032,47.24486,15.16448);
        sAller=new Station("Allerheiligen",8100613,47.482599, 15.402913);
        sNikl=new Station("Niklasdorf",8101275 ,47.394298, 15.155502);
        sKin=new Station("Kindberg",8100030 ,47.501363, 15.448393);
        sUnz=new Station("Unzmarkt", 8100074,47.201285, 14.442332);
        sGraz=new Station("Graz",8100173,47.420,15.251);


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
        stations.add(sUnz);
        stations.add(sGraz);
    }
}
