package com.guts.interview.pokemonapi.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PokemonResponse {
    private Long id;
    private String name;
    @JsonProperty("types") 
    private List<TypeWrapper> types;
    private int weight;
    @JsonProperty("abilities") 
    private List<AbilityWrapperResponse> abilities;

    private EvolutionChainResponse evolutionChain;
}



