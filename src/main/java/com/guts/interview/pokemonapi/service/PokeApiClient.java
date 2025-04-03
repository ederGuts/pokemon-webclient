package com.guts.interview.pokemonapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.guts.interview.pokemonapi.client.EvolutionChainIdResponse;
import com.guts.interview.pokemonapi.client.EvolutionChainResponse;
import com.guts.interview.pokemonapi.client.PokemonListResponse;
import com.guts.interview.pokemonapi.client.PokemonResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PokeApiClient {
    private final WebClient webClient;

    public PokeApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://pokeapi.co/api/v2").build();        
    }

    public Mono<PokemonListResponse> getAllPokemon(int offset, int limit) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/pokemon")
                    .queryParam("offset", offset)
                    .queryParam("limit", limit)
                    .build())
            .retrieve()
            .bodyToMono(PokemonListResponse.class)
            .flatMap(this::mapToPokemonListResponse);
    }

    private Mono<PokemonListResponse> mapToPokemonListResponse(PokemonListResponse response) {
        // Add any necessary mapping logic here
        return Flux.fromIterable(response.getResults())
            .flatMap(pokemon -> getPokemonByName(pokemon.getName())
                    .map(pokemonResponse -> {
                        pokemon.setTypes(pokemonResponse.getTypes());
                        pokemon.setWeight(pokemonResponse.getWeight());
                        pokemon.setAbilities(pokemonResponse.getAbilities());
                        return pokemon;
                    })
                    .onErrorResume(error -> {
                        // Log the error and return the Pokémon without enrichment
                        log.error("Error fetching details for Pokémon {}: {}", pokemon.getName(), error.getMessage());
                        return Mono.just(pokemon); // Return the original Pokémon without additional details
                    }))
            .collectList()
            .map(updatedResults -> {
                response.setResults(updatedResults);
                return response;
            });
    }

    public Mono<PokemonResponse> getPokemonByName(String name) {
        log.info("Fetching Type,Ability for Pokemon: " + name);
        return webClient.get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .bodyToMono(PokemonResponse.class)
                .doOnSuccess(pokemonResponse -> {
                    log.info("Fetched details for Pokemon: " + pokemonResponse.toString());
                })
                .doOnError(onError -> {
                    log.error("Error fetching Pokemon details: " + onError.getLocalizedMessage());
                });
    }

    public Mono<PokemonResponse> getPokemonDetailByName(String name) {
        return  getPokemonByName(name)
                .flatMap(pokemonResponse -> getPokemonEvolutionChainIdByName(pokemonResponse.getName())
                    .map(evolutionChainResponse -> {
                        pokemonResponse.setEvolutionChain(evolutionChainResponse);
                        return pokemonResponse;
                    }))
                .doOnError(onError -> {
            log.error("Error fetching Pokemon details: " + onError.getLocalizedMessage());
        });
    }

    public Mono<EvolutionChainResponse> getPokemonEvolutionChainIdByName(String name) {
        log.info("fetching evolution chain id for Pokemon: " + name);
        return webClient.get()
                .uri("/pokemon-species/{name}", name)
                .retrieve()
                .bodyToMono(EvolutionChainIdResponse.class)
                .flatMap(evolutionChainIdResponse -> {
                    // Extract the evolution chain ID from the response
                    String evolutionChainUrl = evolutionChainIdResponse.getEvolutionChain().getUrl();
                    String evolutionChainId = evolutionChainUrl.substring(evolutionChainUrl.lastIndexOf("/") + 1);
                    log.info("Fetching evolution chain for url: {}", evolutionChainUrl);
                    return getEvolutionChain(evolutionChainUrl);
                })
                .doOnSuccess(pokemonResponse -> {
                    log.info("Fetched details for Pokemon: " + pokemonResponse.toString());
                })
                .doOnError(onError -> {
                    log.error("Error fetching Pokemon details: " + onError.getLocalizedMessage());
                });
    }

    public Mono<EvolutionChainResponse> getEvolutionChain(String id) {
        log.info("Fetching evolution chain for ID: {}", id);
        return webClient.get()
            //.uri("/evolution-chain/{id}", id)
            .uri(id)
            .retrieve()
            .bodyToMono(EvolutionChainResponse.class)
            .doOnSuccess(evolutionChainResponse -> log.info("Fetched evolution chain for ID: {}", id))
            .doOnError(error -> log.error("Error fetching evolution chain for ID {}: {}", id, error.getMessage()));
    }
}

    


   



   

