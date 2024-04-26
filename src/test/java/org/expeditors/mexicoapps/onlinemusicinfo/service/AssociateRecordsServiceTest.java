package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AssociateRecordsServiceTest {

    @Autowired
    private AssociateRecordsService associateRecordsService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private ArtistService artistService;

    List<Artist> artistsList = List.of(
            Artist.builder().name("Julio").birthDate(LocalDate.now()).nationality("Mexican").build(),
            Artist.builder().name("Cesar").birthDate(LocalDate.now()).nationality("American").build()
    );

    List<Track> tracksList = List.of(
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.now()).mediaFileType(MediaFileType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.now()).mediaFileType(MediaFileType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.now()).mediaFileType(MediaFileType.Wav).build()
    );

    @BeforeEach
    public void init() {
        artistService.createArtist(artistsList.get(0));
        artistService.createArtist(artistsList.get(1));

        trackService.createTrack(tracksList.get(0));
        trackService.createTrack(tracksList.get(1));
        trackService.createTrack(tracksList.get(2));
    }

    @Test
    public void createAssociateExistArtistsToExistTrack() {
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        assertEquals(idTrack, newAssociate);

        ResponseTracks responseTracks = associateRecordsService.getAllArtistsByTrack(newAssociate);
        assertEquals(2, responseTracks.getArtists().size());

        System.out.println(responseTracks);
    }

    @Test
    public void createAssociateExistArtistsToNonExistTrack() {
        int idTrack = 1000;
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);
        assertEquals(0,newAssociate);
    }

    @Test
    public void createAssociateNonExistArtistsToExistTrack() {
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = new ArrayList<>(Arrays.asList(1,10000,20000));
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);
        assertEquals(0,newAssociate);
    }

    @Test
    public void createAssociateNonExistArtistsToNonExistTrack() {
        int idTrack = 1000;
        List<Integer> idsArtist = new ArrayList<>(Arrays.asList(1,10000,20000));
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);
        assertEquals(0,newAssociate);
    }


    @Test
    public void deleteAssociateExistArtistToExistTrack(){
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);


        boolean isDelete = this.associateRecordsService.deleteAssociateArtistsFromTrack(idTrack, idsArtist);

        assertTrue(isDelete);

        ResponseTracks responseTracks = associateRecordsService.getAllArtistsByTrack(idTrack);
        assertEquals(0, responseTracks.getArtists().size());
    }

    @Test
    public void deleteAssociateExistArtistToNonExistTrack(){
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        boolean isDelete = this.associateRecordsService.deleteAssociateArtistsFromTrack(1000, idsArtist);

        assertFalse(isDelete);
    }

    @Test
    public void deleteAssociateNonExistArtistToExistTrack(){
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        boolean isDelete = this.associateRecordsService.deleteAssociateArtistsFromTrack(idTrack, new ArrayList<>(Arrays.asList(1,10000,20000)));

        assertFalse(isDelete);
    }

    @Test
    public void deleteAssociateNonExistArtistToNonExistTrack(){
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        boolean isDelete = this.associateRecordsService.deleteAssociateArtistsFromTrack(1000,new ArrayList<>(Arrays.asList(1,10000,20000)));

        assertFalse(isDelete);
    }

    @Test
    public void getAssociateByExistTrack(){
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        ResponseTracks responseTracks = associateRecordsService.getAllArtistsByTrack(idTrack);
        assertEquals(2, responseTracks.getArtists().size());
    }

    @Test
    public void getAssociateByNonExistTrack(){
        ResponseTracks responseTracks = associateRecordsService.getAllArtistsByTrack(1000);
        assertNull(responseTracks);
    }

    @Test
    public void createAssociateTracksToArtist() {
        int idArtist = artistService.createArtist(artistsList.getFirst()).getArtist().getId();
        List<Integer> idsTrack = trackService.getAllTracks().stream().map(m->m.getTrack().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        assertEquals(idArtist, newAssociate);

        ResponseArtist responseArtist = associateRecordsService.getAllTracksByArtist(newAssociate);
        assertEquals(3, responseArtist.getTracks().size());

        System.out.println(responseArtist);
    }

    @Test
    public void createAssociateExistTracksToNonExistArtist() {
        int idArtist = 1000;
        List<Integer> idsTrack = trackService.getAllTracks().stream().map(m->m.getTrack().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);
        assertEquals(0,newAssociate);
    }

    @Test
    public void createAssociateNonExistTracksToExistArtist() {
        int idArtist = artistService.createArtist(artistsList.getFirst()).getArtist().getId();
        List<Integer> idsTrack = new ArrayList<>(Arrays.asList(1,10000,20000));
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);
        assertEquals(0,newAssociate);
    }

    @Test
    public void createAssociateNonExistTracksToNonExistArtist() {
        int idArtist = 1000;
        List<Integer> idsTrack = new ArrayList<>(Arrays.asList(1,10000,20000));
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);
        assertEquals(0,newAssociate);
    }


    @Test
    public void deleteAssociateExistTrackToExistArtist(){
        int idArtist = artistService.createArtist(artistsList.getFirst()).getArtist().getId();
        List<Integer> idsTrack = trackService.getAllTracks().stream().map(m->m.getTrack().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        boolean isDelete = this.associateRecordsService.deleteAssociateTracksFromArtist(idArtist, idsTrack);

        assertTrue(isDelete);

        ResponseArtist responseTracks = associateRecordsService.getAllTracksByArtist(idArtist);
        assertEquals(0, responseTracks.getTracks().size());
    }

    @Test
    public void deleteAssociateExistTrackToNonExistArtist(){
        int idArtist = 1000;
        List<Integer> idsTrack = trackService.getAllTracks().stream().map(m->m.getTrack().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        boolean isDelete = this.associateRecordsService.deleteAssociateTracksFromArtist(idArtist, idsTrack);

        assertFalse(isDelete);
    }

    @Test
    public void deleteAssociateNonExistTrackToExistArtist(){
        int idArtist = artistService.createArtist(artistsList.getFirst()).getArtist().getId();
        List<Integer> idsTrack = new ArrayList<>(Arrays.asList(1,10000,20000));
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        boolean isDelete = this.associateRecordsService.deleteAssociateTracksFromArtist(idArtist, idsTrack);

        assertFalse(isDelete);
    }

    @Test
    public void deleteAssociateNonExistTrackToNonExistArtist(){
        boolean isDelete = this.associateRecordsService.deleteAssociateTracksFromArtist(1000,new ArrayList<>(Arrays.asList(1,10000,20000)));
        assertFalse(isDelete);
    }

    @Test
    public void getAssociateByExistArtist(){
        int idArtist = artistService.createArtist(artistsList.getFirst()).getArtist().getId();
        List<Integer> idsTrack = trackService.getAllTracks().stream().map(m->m.getTrack().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        ResponseArtist responseArtist = associateRecordsService.getAllTracksByArtist(idArtist);
        assertEquals(3, responseArtist.getTracks().size());
    }

    @Test
    public void getAssociateByNonExistArtist(){
        ResponseArtist responseArtist = associateRecordsService.getAllTracksByArtist(1000);
        assertEquals(0, responseArtist.getTracks().size());
    }

}
