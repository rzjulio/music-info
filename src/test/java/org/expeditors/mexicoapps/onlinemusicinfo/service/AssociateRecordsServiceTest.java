package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseTracks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            Track.builder().duration(9.1).album("The first").title("Black").issueDate(LocalDate.now()).mediaType(MediaType.Ogg).build(),
            Track.builder().duration(3.22).album("The second").title("White").issueDate(LocalDate.now()).mediaType(MediaType.MP3).build(),
            Track.builder().duration(4).album("The third").title("Blue").issueDate(LocalDate.now()).mediaType(MediaType.Wav).build()
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
    public void createAssociateArtistsToTrack() {
        int idTrack = trackService.createTrack(tracksList.getFirst()).getTrack().getId();
        List<Integer> idsArtist = artistService.getAllArtist().stream().map(m -> m.getArtist().getId()).toList();
        int newAssociate = this.associateRecordsService.createAssociateArtistsToTrack(idTrack, idsArtist);

        assertEquals(idTrack, newAssociate);

        ResponseTracks responseTracks = associateRecordsService.getAllArtistsByTrack(newAssociate);
        assertEquals(2, responseTracks.getArtists().size());

        System.out.println(responseTracks);
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
}
