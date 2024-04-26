package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TrackServiceTest {

    @Autowired
    private TrackService trackService;


    List<Track> tracks = List.of(
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.parse("2021-01-12")).mediaFileType(MediaFileType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.parse("2020-05-08")).mediaFileType(MediaFileType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.parse("2020-03-02")).mediaFileType(MediaFileType.Wav).build()
    );

    @Test
    public void createTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());
        assertNotNull(newTrack);
    }

    @Test
    public void createTrackNull() {
        ResponseTracks newTrack = trackService.createTrack(null);
        assertNull(newTrack);
    }

    @Test
    public void updateTrackWithExistingTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());

        assertTrue(newTrack.getTrack().getTitle().contains("Black"));

        newTrack.getTrack().setTitle("The new Black");

        boolean result = trackService.updateTrack(newTrack.getTrack());
        assertTrue(result);

        newTrack = trackService.getTrack(newTrack.getTrack().getId());
        assertEquals("The new Black", newTrack.getTrack().getTitle());
    }

//    @Test
//    public void updateTrackWithExistingTrackWithIncorrectFields(){
//        Track incorrectTrack = Track.builder().build();
//        ResponseTracks newTrack = trackService.createTrack(incorrectTrack);
//
//        boolean result = trackService.updateTrack(newTrack.getTrack());
//        assertFalse(result);
//
//    }

    @Test
    public void updateTrackWithNonExistingTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());

        newTrack.getTrack().setId(1000);

        boolean result = trackService.updateTrack(newTrack.getTrack());
        assertFalse(result);

    }

    @Test
    public void deleteTrackWithGoodId(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());
        ResponseTracks track = trackService.getTrack(newTrack.getTrack().getId());

        boolean isDelete = trackService.deleteTrack(track.getTrack().getId());

        assertTrue(isDelete);
    }

    @Test
    public void deleteTrackWithBadId(){
        boolean isDelete = trackService.deleteTrack(1000);

        assertFalse(isDelete);
    }

    @Test
    public void testAllTracks(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getAllTracks();

        assertEquals(3,listTracks.size());
    }

    @Test
    public void getTrackWithGoodId(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());

        ResponseTracks track = trackService.getTrack(newTrack.getTrack().getId());
        assertNotNull(track);
    }

    @Test
    public void getTrackWithBadId(){
        trackService.createTrack(tracks.getFirst());

        ResponseTracks track = trackService.getTrack(10000);
        assertNull(track);
    }

    @Test
    public void testAllTracksByYear(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByYear("2020");

        assertEquals(2,listTracks.size());
    }

    @Test
    public void testAllTracksByYearIncorrect(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByYear("33D");

        assertEquals(0,listTracks.size());
    }
    @Test
    public void testAllTracksByYearNull(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByYear(null);

        assertEquals(0,listTracks.size());
    }

    @Test
    public void testAllTracksByMediaType(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByMediaType(MediaFileType.MP3);

        assertEquals(1,listTracks.size());
    }

    @Test
    public void testAllTracksByMediaTypeNull(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByMediaType(null);

        assertNull(listTracks);
    }

    @Test
    public void testAllTracksByLonger(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("longer",1);

        assertEquals(3,listTracks.size());
    }
    @Test
    public void testAllTracksByShorter(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("shorter",5);

        assertEquals(2,listTracks.size());
    }

    @Test
    public void testAllTracksByEqual(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("equal",4);

        assertEquals(1,listTracks.size());
    }

    @Test
    public void testAllTracksByLongerIncorrect(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("longerBad",1);

        assertEquals(0,listTracks.size());
    }
    @Test
    public void testAllTracksByShorterIncorrect(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("shorterBad",5);

        assertEquals(0,listTracks.size());
    }

    @Test
    public void testAllTracksByEqualIncorrect(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration("equalBad",4);

        assertEquals(0,listTracks.size());
    }

    @Test
    public void testAllTracksByLongerNull(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration(null,1);

        assertEquals(0,listTracks.size());
    }
    @Test
    public void testAllTracksByShorterNull(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration(null,5);

        assertEquals(0,listTracks.size());
    }

    @Test
    public void testAllTracksByEqualNull(){
        trackService.createTrack(tracks.get(0));
        trackService.createTrack(tracks.get(1));
        trackService.createTrack(tracks.get(2));
        List<ResponseTracks> listTracks = trackService.getTracksByDuration(null,4);

        assertEquals(0,listTracks.size());
    }

}
