package org.expeditors.mexicoapps.onlinemusicinfo.controller;

import org.expeditors.mexicoapps.onlinemusicinfo.domain.Artist;
import org.expeditors.mexicoapps.onlinemusicinfo.dto.ResponseArtist;
import org.expeditors.mexicoapps.onlinemusicinfo.service.ArtistService;
import org.expeditors.mexicoapps.onlinemusicinfo.service.AssociateRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    @Autowired
    private ArtistService artistService;
    @Autowired
    private AssociateRecordsService associateRecordsService;
    @Autowired
    private UriCreator uriCreator;

    @GetMapping
    public ResponseEntity<?> getAllArtists() {
        List<ResponseArtist> artists = artistService.getAllArtist();

        if (artists.isEmpty()) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artists found");
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable("id") int id) {
        ResponseArtist artist = artistService.getArtist(id);
        if (artist.getArtist() == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artist with id: " + id);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<?> getArtistByName(@PathVariable("name") String name) {
        ResponseArtist artist = artistService.getArtistByName(name);
        if (artist.getArtist() == null) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artist with name: " + name);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<?> addArtist(@RequestBody Artist artist) {
        ResponseArtist newArtist = artistService.createArtist(artist);

        URI newResource = uriCreator.getURI(newArtist.getArtist().getId());

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable("id") int id) {
        boolean result = artistService.deleteArtist(id);
        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artist with id: " + id);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateArtist(@RequestBody Artist artist) {
        boolean result = artistService.updateArtist(artist);
        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artist with id: " + artist.getId());
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/associateTracks")
    public ResponseEntity<?> associateTracks(@RequestParam int idArtist, @RequestParam List<Integer> idsTrack) {
        int idArtistAssociate = associateRecordsService.createAssociateTracksToArtist(idArtist, idsTrack);

        if (idArtistAssociate < 1){
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"Can't create associate with the track");
            return ResponseEntity.badRequest()
                    .body(pd);
        }

        URI newResource = uriCreator.getURI(idArtistAssociate);

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/disAssociateTracks")
    public ResponseEntity<?> disAssociateTracks(@RequestParam int idArtist, @RequestParam List<Integer> idsTrack) {
        boolean result = associateRecordsService.deleteAssociateTracksFromArtist(idArtist, idsTrack);

        if (!result) {
            var pd = uriCreator.getProblemDetail(HttpStatus.BAD_REQUEST,"No artist with id: " + idArtist);
            return ResponseEntity.badRequest()
                    .body(pd);
        }
        return ResponseEntity.noContent().build();
    }
}
