package org.expeditors.mexicoapps.pricingtrack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/pricing")
public class PriceTrack {

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceForTrack(@PathVariable("id") int id) {
        //Simulate do anything with de id
        DecimalFormat df = new DecimalFormat("###.##");
        double price = ThreadLocalRandom.current().nextDouble(50,300);
        if (id == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You must send a id for the track");
        }
        return ResponseEntity.ok(df.format(price));
    }
}
