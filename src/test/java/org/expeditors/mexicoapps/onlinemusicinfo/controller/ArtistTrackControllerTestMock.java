package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.service.ArtistService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.TrackService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ArtistController.class, TrackController.class })
@AutoConfigureMockMvc
public class ArtistTrackControllerTestMock {
    @MockBean
    private ArtistService artistService;

    @MockBean
    private TrackService trackService;

    @MockBean
    private AssociateRecordsService associateRecordsService;

    @MockBean
    private UriCreator uriCreator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

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
    public void testAddArtistGood() throws Exception {
        Artist artist = Artist.builder().name("Test").birthDate(LocalDate.parse("2021-01-12")).nationality("American").build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(artist);

        Mockito.when(artistService.createArtist(any(Artist.class))).thenReturn(transformClass(artist));
        Mockito.when(uriCreator.getURI(0))
                .thenReturn(new URI("http://localhost:8080/api/artist/0"));

        ResultActions actions = mockMvc.perform(post("/api/artist").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isCreated());

        actions = actions.andExpect(jsonPath("$.artist.name").value(Matchers.containsString("Test")));


        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

        //Mockito.verify(artistService).createArtist(artist);
        Mockito.verify(uriCreator).getURI(0);
    }
}
