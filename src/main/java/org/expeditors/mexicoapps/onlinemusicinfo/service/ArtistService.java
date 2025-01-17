package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.dao.ArtistDao;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {
    @Autowired
    private ArtistDao artistDao;
    @Autowired
    private AssociateRecordsService associateRecordsService;

    public ResponseArtist createArtist(Artist artist) {
        Artist newArtist = this.artistDao.insert(artist);
        if(newArtist == null){
            return null;
        }
        return associateRecordsService.getAllTracksByArtist(newArtist.getId());
    }

    public boolean updateArtist(Artist artist) {
        return this.artistDao.update(artist);
    }

    public boolean deleteArtist(int id){
        return this.artistDao.delete(id);
    }
    public ResponseArtist getArtist(int id){
        return associateRecordsService.getAllTracksByArtist(id);
    }

    public List<ResponseArtist> getAllArtist(){
        List<Artist> artists = this.artistDao.findAll();
        List<ResponseArtist> responseArtists = new ArrayList<>();
        artists.forEach(f ->{
            ResponseArtist responseArtist = associateRecordsService.getAllTracksByArtist(f.getId());
            responseArtists.add(responseArtist);
        });
        return responseArtists;
    }

    public ResponseArtist getArtistByName(String name){
        Artist artist = this.artistDao.findByName(name);
        if(artist==null){
            return null;
        }
        return associateRecordsService.getAllTracksByArtist(artist.getId());
    }

    public List<ResponseArtist> getAllArtistsById(List<Integer> id){
        List<Artist> artists = this.artistDao.findArtistsById(id);
        List<ResponseArtist> responseArtists = new ArrayList<>();
        artists.forEach(f ->{
            ResponseArtist responseArtist = associateRecordsService.getAllTracksByArtist(f.getId());
            responseArtists.add(responseArtist);
        });
        return responseArtists;
    }

}
