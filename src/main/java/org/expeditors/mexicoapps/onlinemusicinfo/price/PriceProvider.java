package org.expeditors.mexicoapps.onlinemusicinfo.price;

import org.expeditors.mexicoapps.onlinemusicinfo.dto.TrackTransform;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class PriceProvider {
    private final RestClient restClient;

    private final String priceUrl;

    public PriceProvider() {
        var baseUrl = "http://localhost:8081";
        var rootUrl = "/api/pricing";
        priceUrl = rootUrl + "/{id}";

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void setPriceToTrack(TrackTransform track){
        ResponseEntity<Double> result =  restClient.get()
                .uri(priceUrl, track.getId())
                .retrieve()
                .toEntity(Double.class);

        if(result.getStatusCode() == HttpStatus.OK){
            double price = Objects.requireNonNull(result.getBody());
            track.setPrice(price);
        }
    }
}
