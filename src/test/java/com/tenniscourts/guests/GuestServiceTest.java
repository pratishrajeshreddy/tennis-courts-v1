package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GuestServiceTest {
    @InjectMocks
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;
    @Mock
    private GuestMapper guestMapper;

    @Test
    public void addGuestTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        Guest guest = Guest.builder().name("Test").build();
        when(guestMapper.map(guestDTO)).thenReturn(guest);
        when(guestMapper.map(guest)).thenReturn(guestDTO);
        when(guestRepository.saveAndFlush(guest)).thenReturn(guest);
        GuestDTO response = guestService.addGuest(guestDTO);
        assertEquals(response.getName(), guestDTO.getName());

    }

    @Test
    public void findByGuestIdTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        Optional<Guest> guest = Optional.of(Guest.builder().name("Test").build());
        when(guestMapper.map(guest.get())).thenReturn(guestDTO);
        when(guestRepository.findById(1l)).thenReturn(guest);
        GuestDTO response = guestService.findByGuestId(1l);
        assertEquals(response.getName(), guestDTO.getName());

    }

    @Test(expected = RuntimeException.class)
    public void findByGuestIdEmptyTest() {
        Optional<Guest> guest = Optional.empty();
        when(guestRepository.findById(1l)).thenThrow(EntityNotFoundException.class);
        guestService.findByGuestId(1l);
    }

    @Test
    public void findByGuestNameTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("TEST").build();
        List<Guest> guests = new ArrayList<>();
        Guest guest = Guest.builder().name("TEST").build();
        guests.add(guest);
        when(guestMapper.map(guest)).thenReturn(guestDTO);
        when(guestRepository.findByName("TEST")).thenReturn(guests);
        List<GuestDTO> response = guestService.findByGuestName("Test");
        assertEquals(response.size(), 1);
    }

    @Test
    public void findByGuestNameEmptyTest() {
        List<Guest> guests = new ArrayList<>();
        Guest guest = Guest.builder().name("TEST").build();
        guests.add(guest);
        when(guestRepository.findByName("Test")).thenReturn(guests);
        List<GuestDTO> response = guestService.findByGuestName("Test");
        assertEquals(response.size(), 0);
    }

    @Test
    public void findAllGuestsTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        List<Guest> guests = new ArrayList<>();
        Guest guest = Guest.builder().name("Test").build();
        guests.add(guest);
        when(guestMapper.map(guest)).thenReturn(guestDTO);
        when(guestRepository.findAll()).thenReturn(guests);
        List<GuestDTO> response = guestService.findAllGuests();
        assertEquals(response.size(), 1);
    }

    @Test
    public void updateGuestTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test1").build();
        Guest guest = Guest.builder().name("Test1").build();
        when(guestMapper.map(guestDTO)).thenReturn(guest);
        when(guestMapper.map(guest)).thenReturn(guestDTO);
        when(guestRepository.existsById(1l)).thenReturn(true);
        when(guestRepository.save(guest)).thenReturn(guest);
        GuestDTO response = guestService.updateGuest(1l, guestDTO);
        assertEquals(response.getName(), guestDTO.getName());
    }

    @Test(expected = RuntimeException.class)
    public void updateGuestEmptyTest() {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test1").build();
        when(guestRepository.existsById(1l)).thenReturn(false);
        guestService.updateGuest(1l, guestDTO);
    }

    @Test
    public void deleteGuestByIdTest() {
        when(guestRepository.existsById(1l)).thenReturn(true);
        doNothing().when(guestRepository).deleteById(1l);
        String response = guestService.deleteGuestByGuestId(1l);
        assertEquals(response, "Successfully deleted guest.");

    }

    @Test(expected = RuntimeException.class)
    public void deleteGuestByIdEmptyTest() {
        when(guestRepository.existsById(1l)).thenReturn(false);
        doNothing().when(guestRepository).deleteById(1l);
        guestService.deleteGuestByGuestId(1l);
    }

    @Test
    public void isGuestExistTest() {
        when(guestRepository.existsById(1l)).thenReturn(true);
        boolean response = guestService.isGuestExist(1l);
        assertEquals(response, true);
    }

    @Test
    public void isGuestExistFailTest() {
        when(guestRepository.existsById(1l)).thenReturn(false);
        boolean response = guestService.isGuestExist(1l);
        assertEquals(response, false);
    }
}
