package com.guts.interview.pokemonapi.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PokemonListResponse {

    private int count;
    private String next;
    private String previous;
    private List<PokemonResponse> results;

}
