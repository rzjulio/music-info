package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.RelTrackArtist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AssociateRecordsImpl implements AssociateRecordsDao {

    private final Map<Integer, RelTrackArtist> relTrackArtistMap = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public List<Integer> findTracksByArtist(int id) {
        return relTrackArtistMap.values().stream().filter(f -> f.getIdArtist() == id).map(RelTrackArtist::getIdTrack).toList();
    }

    @Override
    public List<Integer> findArtistsByTrack(int id) {
        return relTrackArtistMap.values().stream().filter(f -> f.getIdTrack() == id).map(RelTrackArtist::getIdArtist).toList();
    }

    @Override
    public int associateArtists(int idTrack, List<Integer> idsArtist) {
        try {
            idsArtist.forEach(f -> {
                boolean isExistRel = relTrackArtistMap.values().stream().anyMatch(w -> w.getIdTrack() == idTrack && w.getIdArtist() == f);
                if (!isExistRel) {
                    RelTrackArtist relTrackArtist = new RelTrackArtist();
                    relTrackArtist.setId(nextId.getAndIncrement());
                    relTrackArtist.setIdArtist(f);
                    relTrackArtist.setIdTrack(idTrack);
                    relTrackArtistMap.put(relTrackArtist.getId(), relTrackArtist);
                }
            });
            return idTrack;
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public int associateTracks(int idArtist, List<Integer> idsTrack) {
        try {
            idsTrack.forEach(f -> {
                boolean isExist = relTrackArtistMap.values().stream().anyMatch(w -> w.getIdTrack() == idArtist && w.getIdTrack() == f);
                if (!isExist) {
                    RelTrackArtist relTrackArtist = new RelTrackArtist();
                    relTrackArtist.setId(nextId.getAndIncrement());
                    relTrackArtist.setIdTrack(f);
                    relTrackArtist.setIdArtist(idArtist);
                    relTrackArtistMap.put(relTrackArtist.getId(), relTrackArtist);
                }
            });
            return idArtist;
        } catch (RuntimeException e) {
            return 0;
        }

    }

    @Override
    public boolean disAssociateArtists(int idTrack, List<Integer> idsArtist) {
        try {
            List<RelTrackArtist> listRelToRemove = relTrackArtistMap.values().stream().filter(f -> f.getIdTrack() == idTrack && idsArtist.contains(f.getIdArtist())).toList();
            listRelToRemove.forEach(f -> relTrackArtistMap.remove(f.getId(), f));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean disAssociateTracks(int idArtist, List<Integer> idsTrack) {
        try {
            List<RelTrackArtist> listRelToRemove = relTrackArtistMap.values().stream().filter(f -> f.getIdArtist() == idArtist && idsTrack.contains(f.getIdTrack())).toList();
            listRelToRemove.forEach(f -> relTrackArtistMap.remove(f.getId(), f));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
