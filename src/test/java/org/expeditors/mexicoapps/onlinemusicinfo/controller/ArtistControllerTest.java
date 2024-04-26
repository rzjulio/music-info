package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.service.ArtistService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
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
public class ArtistControllerTest {

    @InjectMocks
    private ArtistController artistController;

    @Mock
    private ArtistService artistService;
    @Mock
    private AssociateRecordsService associateRecordsService;
    @Mock
    private UriCreator uriCreator;

    List<Artist> artists = List.of(
            Artist.builder().name("Julio").birthDate(LocalDate.now()).nationality("Mexican").build(),
            Artist.builder().name("Cesar").birthDate(LocalDate.now()).nationality("American").build()
    );

    List<Track> tracks = List.of(
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.parse("2021-01-12")).mediaFileType(MediaFileType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.parse("2020-05-08")).mediaFileType(MediaFileType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.parse("2020-03-02")).mediaFileType(MediaFileType.Wav).build()
    );

    private int goodId = 1;
    private int badId=1000;
    private List<Integer> listInts = new ArrayList<>(List.of(1,2,3));
    private ResponseArtist transformClass(Artist artist){
        return new ResponseArtist(artist,new ArrayList<>());
    }

    @Test
    public void testGetOneArtistGoodJson() throws Exception {
        Mockito.when(artistService.getArtist(1)).thenReturn(transformClass(artists.getFirst()));
        ResponseEntity<?> response = artistController.getArtistById(1);

        var status = HttpStatus.CREATED.value();
        assertEquals(200, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(artistService).getArtist(1);

    }

    @Test
    public void testGetOneArtistByName() throws Exception {
        Mockito.when(artistService.getArtistByName("Julio")).thenReturn(transformClass(artists.getFirst()));
        ResponseEntity<?> response = artistController.getArtistByName("Julio");

        var status = HttpStatus.OK.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(artistService).getArtistByName("Julio");

    }

    @Test
    public void testGetAllArtist(){

        List<ResponseArtist> responseArtists = artists.stream().map(this::transformClass).toList();
        Mockito.when(artistService.getAllArtist()).thenReturn(responseArtists);

        var status = HttpStatus.OK.value();
        ResponseEntity<?> response = artistController.getAllArtists();
        assertEquals(status, response.getStatusCodeValue());

        assertEquals(2, responseArtists.size());

        Mockito.verify(artistService).getAllArtist();
    }

    @Test
    public void testGetOneArtistByBadName() throws Exception {
        Mockito.when(artistService.getArtistByName("Bad")).thenReturn(transformClass(null));
        ResponseEntity<?> response = artistController.getArtistByName("Bad");

        var status = HttpStatus.BAD_REQUEST.value();
        assertEquals(status, response.getStatusCodeValue());

        System.out.println(response);
        Mockito.verify(artistService).getArtistByName("Bad");

    }

    @Test
    public void testGetOneArtistBadId() throws Exception {
        Mockito.when(artistService.getArtist(1000)).thenReturn(new ResponseArtist(null,new ArrayList<>()));
        var status = HttpStatus.BAD_REQUEST.value();

        ResponseEntity<?> response = artistController.getArtistById(1000);
        assertEquals(status, response.getStatusCodeValue());
        Mockito.verify(artistService).getArtist(1000);
    }

    @Test
    public void testAddArtistGood() throws Exception {
        Artist artist = artists.getFirst();
        artist.setId(1);

        URI uri = new URI("http://localhost:8080/api/artist/1");

        Mockito.when(artistService.createArtist(artist)).thenReturn(transformClass(artist));
        Mockito.when(uriCreator.getURI(1)).thenReturn(uri);

        var status = HttpStatus.CREATED.value();
        ResponseEntity<?> response = artistController.addArtist(artist);
        assertEquals(status, response.getStatusCodeValue());
        assertEquals(uri, response.getHeaders().getLocation());

        Mockito.verify(artistService).createArtist(artist);
    }

    @Test
    public void testUpdateArtistGood() throws Exception {
        Artist artist = artists.get(0);
        artist.setId(1);

        Mockito.when(artistService.updateArtist(artist)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = artistController.updateArtist(artist);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(artistService).updateArtist(artist);
    }

    @Test
    public void testUpdateArtistBad() throws Exception {
        Artist artist = artists.get(0);
        artist.setId(1000);

        Mockito.when(artistService.updateArtist(artist)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = artistController.updateArtist(artist);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(artistService).updateArtist(artist);
    }

    @Test
    public void testDeleteArtistGood() throws Exception {

        Mockito.when(artistService.deleteArtist(1)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = artistController.deleteArtist(1);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(artistService).deleteArtist(1);
    }

    @Test
    public void testDeleteArtistBad() throws Exception {

        Mockito.when(artistService.deleteArtist(1000)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = artistController.deleteArtist(1000);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(artistService).deleteArtist(1000);

    }

    @Test
    public void testAssociateTracksToArtist() throws Exception {
        Artist artist = artists.getFirst();
        artist.setId(goodId);
        artists.forEach(f -> f.setId(artists.indexOf(f)+1));

        List<Integer> idsTrack = tracks.stream().map(Track::getId).toList();
        URI uri = new URI("http://localhost:8080/api/artist/"+artist.getId());
        Mockito.when(associateRecordsService.createAssociateTracksToArtist(artist.getId(),idsTrack)).thenReturn(artist.getId());
        Mockito.when(uriCreator.getURI(artist.getId())).thenReturn(uri);

        var status = HttpStatus.CREATED.value();
        ResponseEntity<?> response = artistController.associateTracks(artist.getId(),idsTrack);
        assertEquals(status, response.getStatusCodeValue());
        assertEquals(uri, response.getHeaders().getLocation());

        Mockito.verify(associateRecordsService).createAssociateTracksToArtist(artist.getId(),idsTrack);
        Mockito.verify(uriCreator).getURI(artist.getId());

    }

    @Test
    public void testAssociateTracksToNonExistArtist() throws Exception {

        Mockito.when(associateRecordsService.createAssociateTracksToArtist(badId,listInts)).thenReturn(0);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = artistController.associateTracks(badId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).createAssociateTracksToArtist(badId,listInts);

    }

    @Test
    public void testDeleteAssociateTracksToArtist() throws Exception {
        Mockito.when(associateRecordsService.deleteAssociateTracksFromArtist(goodId,listInts)).thenReturn(true);

        var status = HttpStatus.NO_CONTENT.value();
        ResponseEntity<?> response = artistController.disAssociateTracks(goodId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).deleteAssociateTracksFromArtist(goodId,listInts);

    }


    @Test
    public void testDeleteAssociateTracksToNonExistArtist() throws Exception {

        Mockito.when(associateRecordsService.deleteAssociateTracksFromArtist(badId,listInts)).thenReturn(false);

        var status = HttpStatus.BAD_REQUEST.value();
        ResponseEntity<?> response = artistController.disAssociateTracks(badId,listInts);
        assertEquals(status, response.getStatusCodeValue());

        Mockito.verify(associateRecordsService).deleteAssociateTracksFromArtist(badId,listInts);

    }

}
