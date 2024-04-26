package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.dao.ArtistDao;
import org.expeditors.mexicoapps.onlinemusicinfo.dao.AssociateRecordsDao;
import org.expeditors.mexicoapps.onlinemusicinfo.dao.TrackDao;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.TrackTransform;
import org.expeditors.mexicoapps.onlinemusicinfo.price.PriceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssociateRecordsService {
    @Autowired
    private AssociateRecordsDao associateRecords;
    @Autowired
    private ArtistDao artistDao;
    @Autowired
    private TrackDao trackDao;
    @Autowired
    private PriceProvider priceProvider;

    public ResponseArtist getAllTracksByArtist(int idArtist) {
        List<Integer> idsTrack = this.associateRecords.findTracksByArtist(idArtist);
        List<Track> tracks = trackDao.findTracksById(idsTrack);
        Artist artist = artistDao.findById(idArtist);
        List<TrackTransform> trackTransforms = tracks.stream().map(this::transformClass).toList();

        return new ResponseArtist(artist,trackTransforms);
    }

    public ResponseTracks getAllArtistsByTrack(int idTrack) {
        List<Integer> idsArtist = this.associateRecords.findArtistsByTrack(idTrack);
        List<Artist> artists = artistDao.findArtistsById(idsArtist);
        Track track = trackDao.findById(idTrack);
        if(track!=null){
            TrackTransform trackTransform = transformClass(track);
            return new ResponseTracks(trackTransform,artists);
        }
        return null;
    }

    public int createAssociateArtistsToTrack(int idTrack, List<Integer> idsArtist) {
        if (isNotValidArtist(idsArtist) || isNotValidTrack(new ArrayList<>(List.of(idTrack)))) {
            return 0;
        }

        return this.associateRecords.associateArtists(idTrack, idsArtist);
    }

    public int createAssociateTracksToArtist(int idArtist, List<Integer> idsTrack) {
        if (isNotValidTrack(idsTrack) || isNotValidArtist(new ArrayList<>(List.of(idArtist)))) {
            return 0;
        }
        return this.associateRecords.associateTracks(idArtist, idsTrack);
    }

    public boolean deleteAssociateArtistsFromTrack(int idTrack, List<Integer> idsArtist) {
        if (isNotValidArtist(idsArtist) || isNotValidTrack(new ArrayList<>(List.of(idTrack)))) {
            return false;
        }

        return this.associateRecords.disAssociateArtists(idTrack, idsArtist);
    }

    public boolean deleteAssociateTracksFromArtist(int idArtist, List<Integer> idsTrack) {
        if (isNotValidTrack(idsTrack) || isNotValidArtist(new ArrayList<>(List.of(idArtist)))) {
            return false;
        }

        return this.associateRecords.disAssociateTracks(idArtist, idsTrack);
    }

    private boolean isNotValidArtist(List<Integer> id) {
        return artistDao.findArtistsById(id).stream().noneMatch(f -> id.contains(f.getId()));
    }

    private boolean isNotValidTrack(List<Integer> id) {
        return trackDao.findTracksById(id).stream().noneMatch(f -> id.contains(f.getId()));
    }

    private TrackTransform transformClass(Track track){
        TrackTransform trackTransform = new TrackTransform(track.getId(),track.getTitle(),track.getAlbum(),track.getIssueDate(),track.getDuration(),track.getMediaFileType(),0);
        priceProvider.setPriceToTrack(trackTransform);
        return trackTransform;
    }
}
