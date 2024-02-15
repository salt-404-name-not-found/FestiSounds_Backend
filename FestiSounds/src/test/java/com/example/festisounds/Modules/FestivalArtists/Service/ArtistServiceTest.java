package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Core.Exceptions.FestivalArtists.ArtistNotFoundException;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistRequestDTO;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @InjectMocks
    ArtistService artistService;

    @Mock
    FestivalArtistRepository artistRepository;

    private Festival festival1;
    private ArtistRequestDTO artistRequest;
    private ArtistResponseDTO artistResponse1;
    private FestivalArtist artistEntity1;
    private UUID artistEntity1ID;
    private String artistName1;
    private String spotifyId1;
    private Set<String> genres;

    @BeforeEach
    void setup() {
        artistName1 = "artist1";
        spotifyId1 = "spotifyId1";
        genres = Set.of("test1");
        artistEntity1ID = UUID.randomUUID();

        festival1 = new Festival();
        festival1.setName("festival1");
        festival1.setId(UUID.randomUUID());
        festival1.setCity("festivalCity");
        festival1.setDetails("test");
        festival1.setCountry("festival country");
        festival1.setEndDate(new Date());
        festival1.setStartDate(new Date());
        festival1.setWebsite("website");
        festival1.setOrganizer("organizer");

        artistRequest = new ArtistRequestDTO(spotifyId1,
                artistName1,
                genres);

        artistEntity1 = new FestivalArtist();
        artistEntity1.setId(artistEntity1ID);
        artistEntity1.setArtistName(artistName1);
        artistEntity1.setFestivals(Set.of(festival1));
        artistEntity1.setSpotifyId(spotifyId1);
        artistEntity1.setGenres(genres);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "tom odell", "pawsa", "the lumineers", "avicii" })
    @DisplayName("Filter out right data from spotify")
    void findArtistInSpotifyAndCreateArtistObject_whenGivenArtistArray_returnsCorrectData(String name, Artist[] foundArtists) {

        ArtistRequestDTO artist = artistService.findArtistInSpotifyAndCreateArtistObject(name, foundArtists);

        Assertions.assertNotNull(artist, () -> "Artist should exist");
        Assertions.assertEquals(foundArtists[0].getName(), name, () -> "Artist name does not match");
        Assertions.assertTrue(artist.genres().size() > 0, () -> "Artist should have at least one genre");

    }

    @Test
    void createArtist_whenGivenArtistNewData_createArtist() {
        when(artistRepository.save(any(FestivalArtist.class))).thenReturn(artistEntity1);

        ArtistResponseDTO createdArtist = artistService.createArtist(festival1, artistRequest);

        verify(artistRepository).save(any(FestivalArtist.class));
        Assertions.assertEquals(artistEntity1.getArtistName(), createdArtist.artistName(),
                () -> "Artists name does not match");
    }

    @Test
    void createOrAddArtistRouter_whenArtistDoesNotExist_createNewArtist() {

    }

    @Test
    @DisplayName("Get artist by ID")
    void getArtist_whenGivenExistingArtistId_returnsArtist() {
        // Arrange
        when(artistRepository.findById(artistEntity1ID)).thenReturn(Optional.ofNullable(artistEntity1));

        // Act
        ArtistResponseDTO result = artistService.getArtist(artistEntity1ID);

        // Assert
        assertEquals(artistEntity1.getId(), result.id());
        verify(artistRepository).findById(artistEntity1ID);
    }

    @Test
    @DisplayName("Get artist by non existing ID")
    void getArtist_whenGivenNonExistingID_throwsException() {
        when(artistRepository.findById(artistEntity1ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ArtistNotFoundException.class,
                () -> artistService.getArtist(artistEntity1ID));

        assertTrue(exception.getMessage().contains("Could not find artist with id " + artistEntity1ID));
    }


}
