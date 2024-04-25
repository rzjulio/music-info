package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import java.util.List;

public interface AssociateRecordsDao{
    List<Integer> findTracksByArtist(int id);
    List<Integer> findArtistsByTrack(int id);
    int associateArtists(int idTrack, List<Integer> idsArtist);
    int associateTracks(int idArtist, List<Integer> idsTrack);
    boolean disAssociateArtists(int idTrack, List<Integer> idsArtist);
    boolean disAssociateTracks(int idArtist, List<Integer> idsTrack);
}
