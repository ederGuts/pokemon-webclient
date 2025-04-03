package com.guts.interview.pokemonapi.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionChainResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("chain")
    private Chain chain;

    @Getter
    @Setter
    public static class Chain {
        @JsonProperty("species")
        private Species species;

        @JsonProperty("evolves_to")
        private List<Chain> evolvesTo;

        @Getter
        @Setter
        public static class Species {
            @JsonProperty("name")
            private String name;

            @JsonProperty("url")
            private String url;
        }
    }
}
