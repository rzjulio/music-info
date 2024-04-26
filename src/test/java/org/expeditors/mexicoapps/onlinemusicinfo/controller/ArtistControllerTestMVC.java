package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.MediaFileType;
import org.expeditors.mexicoapps.onlinemusicinfo.domain.Track;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.service.ArtistService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ArtistController.class })
@AutoConfigureMockMvc
public class ArtistControllerTestMVC {
    @Autowired
    private ApplicationContext context;
    @MockBean
    private ArtistService artistService;
    @MockBean
    private AssociateRecordsService associateRecordsService;
    @MockBean
    private UriCreator uriCreator;

    @Autowired
    private MockMvc mockMvc;

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
        MediaType accept = MediaType.APPLICATION_JSON;
        MediaType contentType = accept;

        Mockito.when(artistService.getArtist(goodId)).thenReturn(transformClass(artists.getFirst()));

        MockHttpServletRequestBuilder builder = get("/api/artist/{id}", goodId)
                .accept(accept)
                .contentType(contentType);

        ResultActions actions = mockMvc.perform(builder);

        actions = actions.andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.artist.name").value(containsString("Julio")));

        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();
        String jsonString = response.getContentAsString();

        Mockito.verify(artistService).getArtist(goodId);
    }

    @Test
    public void testGetOneArtistByName() throws Exception {
        MediaType accept = MediaType.APPLICATION_JSON;
        MediaType contentType = accept;

        Mockito.when(artistService.getArtistByName("Julio")).thenReturn(transformClass(artists.getFirst()));

        MockHttpServletRequestBuilder builder = get("/api/artist/byName/{name}", "Julio")
                .accept(accept)
                .contentType(contentType);

        ResultActions actions = mockMvc.perform(builder);

        actions = actions.andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.artist.name").value(containsString("Julio")));

        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();
        String jsonString = response.getContentAsString();

        Mockito.verify(artistService).getArtistByName("Julio");
    }

    @Test
    public void testGetAllArtist() throws Exception {
        List<ResponseArtist> responseArtists = artists.stream().map(this::transformClass).toList();
        Mockito.when(artistService.getAllArtist()).thenReturn(responseArtists);

        ResultActions actions = mockMvc.perform(get("/api/artist").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$", hasSize(2)));
        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        String jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

        Mockito.verify(artistService).getAllArtist();
    }

    @Test
    public void testGetOneArtistByBadName() throws Exception {

        Mockito.when(artistService.getArtistByName("BadName")).thenReturn(transformClass(null));

        MockHttpServletRequestBuilder builder = get("/api/artist/byName/{name}", "BadName")
                .accept(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder);

        actions = actions.andExpect(status().isBadRequest());

        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();
        String jsonString = response.getContentAsString();

        Mockito.verify(artistService, never()).getArtistByName("Julio");

    }

    @Test
    public void testAddArtistGood() throws Exception {
        Artist artist = Artist.builder().name("Test").birthDate(LocalDate.parse("2021-01-12")).nationality("American").build();
//        artist.setId(goodId);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(artist);

        Mockito.when(artistService.createArtist(any(Artist.class))).thenReturn(transformClass(artist));

        ResultActions actions = mockMvc.perform(post("/api/artist").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        //actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isCreated());

//        actions = actions.andExpect(jsonPath("").value(Matchers.containsString("Julio")));


        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

        //Mockito.verify(artistService).createArtist(artist);

    }

    @Test
    public void testUpdateArtistGood() throws Exception {
        Artist artist = artists.getFirst();
        artist.setId(goodId);
        artist.setName("Julio Cesar");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(artist);

        Mockito.when(artistService.updateArtist(any(Artist.class))).thenReturn(true);

        ResultActions actions = mockMvc.perform(put("/api/artist").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

//        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isNoContent());

//        actions = actions.andExpect(jsonPath("$.artist").value(Matchers.containsString("Julio Cesar")));


        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

        //Mockito.verify(artistService).updateArtist(artist);

    }
}
