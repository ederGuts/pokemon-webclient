package com.guts.interview.pokemonapi.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guts.interview.pokemonapi.client.PokemonListResponse;
import com.guts.interview.pokemonapi.client.PokemonResponse;
import com.guts.interview.pokemonapi.service.PokeApiClient;

import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

@RestController
public class PokemonController {

    public PokemonController(PokeApiClient pokeApiClient) {
        this.pokeApiClient = pokeApiClient;
    }
    private final PokeApiClient pokeApiClient;

    
    @GetMapping("/pokemon")
    public Mono<PokemonListResponse> getAllPokemon(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return pokeApiClient.getAllPokemon(offset, limit);
    }

    @GetMapping("/pokemon/{name}")
    public Mono<PokemonResponse> getPokemonByName(@PathVariable @NotBlank String name) {
        return pokeApiClient.getPokemonDetailByName(name);
    }

    

}
