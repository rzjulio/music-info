package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaType;
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
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.now()).mediaType(MediaType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.now()).mediaType(MediaType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.now()).mediaType(MediaType.Wav).build()
    );

    @Test
    public void createTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());
        assertNotNull(newTrack);
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
        assertNull(track.getTrack());
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
    public void updateTrackWithExistingTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());

        assertTrue(newTrack.getTrack().getTitle().contains("Black"));

        newTrack.getTrack().setTitle("The new Black");

        boolean result = trackService.updateTrack(newTrack.getTrack());
        assertTrue(result);

        newTrack = trackService.getTrack(newTrack.getTrack().getId());
        assertEquals("The new Black", newTrack.getTrack().getTitle());
    }

    @Test
    public void updateTrackWithNonExistingTrack(){
        ResponseTracks newTrack = trackService.createTrack(tracks.getFirst());

        newTrack.getTrack().setId(1000);

        boolean result = trackService.updateTrack(newTrack.getTrack());
        assertFalse(result);

    }
}
