package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.dao.TrackDao;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackService {

    @Autowired
    private TrackDao trackDao;
    @Autowired
    private AssociateRecordsService associateRecordsService;

    public ResponseTracks createTrack(Track track){
        Track newTrack = this.trackDao.insert(track);
        if(newTrack == null){
            return null;
        }
        return associateRecordsService.getAllArtistsByTrack(newTrack.getId());
    }

    public boolean updateTrack(Track track) {
        return this.trackDao.update(track);
    }

    public boolean deleteTrack(int id){
        return this.trackDao.delete(id);
    }

    public ResponseTracks getTrack(int id){
        return associateRecordsService.getAllArtistsByTrack(id);
    }

    public List<ResponseTracks> getAllTracks(){
        return transformResponse(this.trackDao.findAll());
    }

    public List<ResponseTracks> getAllTracksById(List<Integer> id){
        return transformResponse(this.trackDao.findTracksById(id));
    }

    public List<ResponseTracks> getTracksByMediaType(MediaFileType value){
        if(value == null){
            return null;
        }

        List<Track> tracks = this.trackDao.findTracksByAnyField(value.toString(),"mediaType");
        return transformResponse(tracks);
    }

    public List<ResponseTracks> getTracksByYear(String value){
        List<Track> tracks = this.trackDao.findTracksByAnyField(value,"year");
        return transformResponse(tracks);
    }

    public List<ResponseTracks> getTracksByDuration(String selectBy, double duration){
        List<Track> tracks = this.trackDao.findTracksByAnyField(selectBy+":"+duration,"duration");
        return transformResponse(tracks);
    }

    private List<ResponseTracks> transformResponse(List<Track> tracks){
        List<ResponseTracks> responseTracks = new ArrayList<>();
        tracks.forEach(f ->{
            ResponseTracks responseTrack = associateRecordsService.getAllArtistsByTrack(f.getId());
            responseTracks.add(responseTrack);
        });
        return responseTracks;
    }
}
