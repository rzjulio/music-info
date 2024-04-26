package org.expeditors.mexicoapps.onlinemusicinfo.service;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArtistServiceTest {
    @Autowired
    private ArtistService artistService;


    List<Artist> artists = List.of(
            Artist.builder().name("Julio").birthDate(LocalDate.now()).nationality("Mexican").build(),
            Artist.builder().name("Cesar").birthDate(LocalDate.now()).nationality("American").build()
    );

    @Test
    public void createArtist() {
        ResponseArtist newArtist = artistService.createArtist(artists.getFirst());
        assertNotNull(newArtist);
    }

    @Test
    public void createArtistNull() {
        ResponseArtist newArtist = artistService.createArtist(null);
        assertNull(newArtist);
    }

    @Test
    public void updateArtistWithExistingArtist() {
        ResponseArtist newArtist = artistService.createArtist(artists.getFirst());

        assertTrue(newArtist.getArtist().getName().contains("Julio"));

        newArtist.getArtist().setName("The new Julio");

        boolean result = artistService.updateArtist(newArtist.getArtist());
        assertTrue(result);

        newArtist = artistService.getArtist(newArtist.getArtist().getId());
        assertEquals("The new Julio", newArtist.getArtist().getName());
    }

//    @Test
//    public void updateArtistWithExistingArtistWithIncorrectFields() {
//        Artist incorrectArtist = Artist.builder().build();
//        ResponseArtist newArtist = artistService.createArtist(incorrectArtist);
//
//        boolean result = artistService.updateArtist(newArtist.getArtist());
//        assertFalse(result);
//    }

    @Test
    public void updateArtistWithNonExistingArtist() {
        ResponseArtist newArtist = artistService.createArtist(artists.getFirst());

        newArtist.getArtist().setId(1000);

        boolean result = artistService.updateArtist(newArtist.getArtist());
        assertFalse(result);

    }

    @Test
    public void deleteArtistWithGoodId() {
        ResponseArtist newTrack = artistService.createArtist(artists.getFirst());
        ResponseArtist artist = artistService.getArtist(newTrack.getArtist().getId());

        boolean isDelete = artistService.deleteArtist(artist.getArtist().getId());

        assertTrue(isDelete);
    }

    @Test
    public void deleteArtistWithBadId() {
        boolean isDelete = artistService.deleteArtist(1000);

        assertFalse(isDelete);
    }

    @Test
    public void testAllArtist() {
        artistService.createArtist(artists.get(0));
        artistService.createArtist(artists.get(1));
        List<ResponseArtist> listArtist = artistService.getAllArtist();

        assertEquals(2, listArtist.size());
    }

    @Test
    public void getArtistWithGoodId() {
        ResponseArtist newArtist = artistService.createArtist(artists.getFirst());

        ResponseArtist artist = artistService.getArtist(newArtist.getArtist().getId());
        assertNotNull(artist);
    }

    @Test
    public void getArtistWithBadId() {
        artistService.createArtist(artists.getFirst());

        ResponseArtist artist = artistService.getArtist(10000);
        assertNull(artist.getArtist());
    }
    @Test
    public void getArtistWithGoodName() {
        ResponseArtist newArtist = artistService.createArtist(artists.getFirst());
        ResponseArtist artist = artistService.getArtistByName("Julio");
        assertNotNull(artist);
        assertEquals("Julio",artist.getArtist().getName());
    }

    @Test
    public void getArtistWithBadName() {
        artistService.createArtist(artists.getFirst());

        ResponseArtist artist = artistService.getArtistByName("Bad name");
        assertNull(artist);
    }
}
