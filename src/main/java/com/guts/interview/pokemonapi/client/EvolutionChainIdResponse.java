package com.guts.interview.pokemonapi.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EvolutionChainIdResponse {

    @JsonProperty("evolution_chain")
    private EvolutionChain evolutionChain;

    @Getter
    @Setter
    public static class EvolutionChain {
        @JsonProperty("url")
        private String url;
    }

}
