package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.TrackTransform;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.TrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {
    @InjectMocks
    private TrackController trackController;

    @Mock
    private TrackService trackService;
    @Mock
    private AssociateRecordsService associateRecordsService;
    @Mock
    private UriCreator uriCreator;

    private int goodId = 1;
    private int badId=1000;
    private List<Integer> listInts = new ArrayList<>(List.of(1,2,3));

    List<Track> tracks = List.of(
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.parse("2021-01-12")).mediaFileType(MediaFileType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.parse("2020-05-08")).mediaFileType(MediaFileType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.parse("2020-03-02")).mediaFileType(MediaFileType.Wav).build()
    );

    List<Artist> artists = List.of(
            Artist.builder().name("Julio").birthDate(LocalDate.now()).nationality("Mexican").build(),
            Artist.builder().name("Cesar").birthDate(LocalDate.now()).nationality("American").build()
    );
    private ResponseTracks transformClass(Track track){
        TrackTransform trackTransform = new TrackTransform(track.getId(),track.getTitle(),track.getAlbum(),track.getIssueDate(),track.getDuration(),track.getMediaFileType(),0);

        return new ResponseTracks(trackTransform,new ArrayList<>());
    }

    @Test
    public void testGetOneTracktGoodJson() throws Exception {
        Track track = tracks.getFirst();
        track.setId(goodId);

        Mockito.when(trackService.getTrack(goodId)).thenReturn(transformClass(track));

        ResponseEntity<?> response = trackController.getTrackById(goodId);

        var status = HttpStatus.OK.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(trackService).getTrack(goodId);
    }

    @Test
    public void testGetOneTrackBadId() throws Exception {
        Mockito.when(trackService.getTrack(badId)).thenReturn(null);
        var status = HttpStatus.BAD_REQUEST.value();

        ResponseEntity<?> response = trackController.getTrackById(badId);
        assertEquals(status, response.getStatusCodeValue());
        Mockito.verify(trackService).getTrack(badId);
    }

    @Test
    public void testGetAllTracks(){

        List<ResponseTracks> responseTracks = tracks.stream().map(this::transformClass).toList();
        Mockito.when(trackService.getAllTracks()).thenReturn(responseTracks);

        var status = HttpStatus.OK.value();
        ResponseEntity<?> response = trackController.getAllTracks();
        assertEquals(status, response.getStatusCodeValue());

        assertEquals(3, responseTracks.size());

        Mockito.verify(trackService).getAllTracks();
    }

    @Test
    public void testAddTrackGood() throws Exception {
        Track track = tracks.getFirst();
        track.setId(goodId);

        URI uri = new URI("http://localhost:8080/api/track/1");

        Mockito.when(trackService.createTrack(track)).thenReturn(transformClass(track));
        Mockito.when(uriCreator.getURI(goodId)).thenReturn(uri);

        var status = HttpStatus.CREATED.value();
        ResponseEntity<?> response = trackController.addTrack(track);
        assertEquals(status, response.getStatusCodeValue());
        assertEquals(uri, response.getHeaders().getLocation());

        Mockito.verify(trackService).createTrack(track);
    }

    @Test
    public void testUpdateTrackGood() throws Exception {
        Track track = tracks.get(0);
        track.setId(goodId);

        Mockito.when(trackService.updateTrack(track)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = trackController.updateTrack(track);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(trackService).updateTrack(track);
    }

    @Test
    public void testUpdateTrackBad() throws Exception {
        Track track = tracks.get(0);
        track.setId(badId);

        Mockito.when(trackService.updateTrack(track)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = trackController.updateTrack(track);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(trackService).updateTrack(track);
    }

    @Test
    public void testDeleteTrackGood() throws Exception {

        Mockito.when(trackService.deleteTrack(goodId)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = trackController.deleteTrack(goodId);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(trackService).deleteTrack(goodId);
    }

    @Test
    public void testDeleteTrackBad() throws Exception {

        Mockito.when(trackService.deleteTrack(badId)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = trackController.deleteTrack(badId);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(trackService).deleteTrack(badId);
    }

    @Test
    public void testGetTrackByMediaType() throws Exception {
        List<ResponseTracks> responseTracks = new ArrayList<>();
        tracks.stream().filter(f-> f.getMediaFileType()== MediaFileType.Ogg).forEach(f -> {
            ResponseTracks responseTrack = transformClass(f);
            responseTracks.add(responseTrack);
        });

        assertEquals(1,responseTracks.size());

        Mockito.when(trackService.getTracksByMediaType(MediaFileType.Ogg)).thenReturn(responseTracks);

        ResponseEntity<?> response = trackController.getTracksByMediaType(MediaFileType.Ogg);

        var status = HttpStatus.OK.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(trackService).getTracksByMediaType(MediaFileType.Ogg);
    }

    @Test
    public void testGetTrackByYear() throws Exception {
        List<ResponseTracks> responseTracks = new ArrayList<>();
        tracks.stream().filter(f-> f.getIssueDate().getYear() == 2020).forEach(f -> {
            ResponseTracks responseTrack = transformClass(f);
            responseTracks.add(responseTrack);
        });

        assertEquals(2,responseTracks.size());

        Mockito.when(trackService.getTracksByYear("2020")).thenReturn(responseTracks);

        ResponseEntity<?> response = trackController.getTracksByYear(2020);

        var status = HttpStatus.OK.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(trackService).getTracksByYear("2020");
    }

    @Test
    public void testGetTrackByDuration() throws Exception {
        List<ResponseTracks> responseTracks = new ArrayList<>();
        tracks.stream().filter(f-> f.getDuration() > 5).forEach(f -> {
            ResponseTracks responseTrack = transformClass(f);
            responseTracks.add(responseTrack);
        });

        assertEquals(1,responseTracks.size());

        Mockito.when(trackService.getTracksByDuration("longer",5)).thenReturn(responseTracks);

        ResponseEntity<?> response = trackController.getTracksByDuration("longer","5");

        var status = HttpStatus.OK.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(trackService).getTracksByDuration("longer",5);
    }

    @Test
    public void testAssociateArtistToTrack() throws Exception {
        Track track = tracks.getFirst();
        track.setId(goodId);
        artists.forEach(f -> f.setId(artists.indexOf(f)+1));

        List<Integer> idsArtist = artists.stream().map(Artist::getId).toList();
        URI uri = new URI("http://localhost:8080/api/track/getArtistsByIdTrack/"+track.getId());
        Mockito.when(associateRecordsService.createAssociateArtistsToTrack(track.getId(),idsArtist)).thenReturn(track.getId());
        Mockito.when(uriCreator.getURI(track.getId())).thenReturn(uri);

        var status = HttpStatus.CREATED.value();
        ResponseEntity<?> response = trackController.associateArtist(track.getId(),idsArtist);
        assertEquals(status, response.getStatusCodeValue());
        assertEquals(uri, response.getHeaders().getLocation());

        Mockito.verify(associateRecordsService).createAssociateArtistsToTrack(track.getId(),idsArtist);
        Mockito.verify(uriCreator).getURI(track.getId());

    }

    @Test
    public void testAssociateArtistToNonExistTrack() throws Exception {

        Mockito.when(associateRecordsService.createAssociateArtistsToTrack(badId,listInts)).thenReturn(0);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = trackController.associateArtist(badId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).createAssociateArtistsToTrack(badId,listInts);

    }

    @Test
    public void testDeleteAssociateArtistToTrack() throws Exception {
        Mockito.when(associateRecordsService.deleteAssociateArtistsFromTrack(goodId,listInts)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = trackController.disAssociateArtist(goodId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).deleteAssociateArtistsFromTrack(goodId,listInts);

    }


    @Test
    public void testDeleteAssociateArtistToNonExistTrack() throws Exception {

        Mockito.when(associateRecordsService.deleteAssociateArtistsFromTrack(badId,listInts)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = trackController.disAssociateArtist(badId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).deleteAssociateArtistsFromTrack(badId,listInts);

    }
}
