package com.tenniscourts.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.*;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GuestController.class)
public class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @MockBean
    private GuestRepository guestRepository;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addGuestTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        Guest guest = Guest.builder().name("Test").build();
        when(guestService.addGuest(any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(post("/guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(guestDTO.getName()));
    }

    @Test
    public void addGuestWithEmptyValueTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("").build();
        Guest guest = Guest.builder().name("Test").build();
        when(guestService.addGuest(any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(post("/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addGuestWithNullValueTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name(null).build();
        Guest guest = Guest.builder().name("Test").build();
        when(guestService.addGuest(any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(post("/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findGuestByIdTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        when(guestService.isGuestExist(1l)).thenReturn(true);
        when(guestService.findByGuestId(any(Long.class))).thenReturn(guestDTO);
        mockMvc.perform(get("/guest/{id}", 1l).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(guestDTO.getName()));
    }

    @Test
    public void findGuestByIdEmptyTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name(null).build();
        when(guestService.findByGuestId(any(Long.class))).thenReturn(guestDTO);
        mockMvc.perform(get("/guest/{id}", 1l).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Guest not found.")));
    }

    @Test
    public void findGuestByNameTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        List<GuestDTO> guests = new ArrayList<>();
        guests.add(guestDTO);
        when(guestService.findByGuestName(any(String.class))).thenReturn(guests);
        mockMvc.perform(get("/guest/name/{name}", "Test").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(guestDTO.getName()));
    }

    @Test
    public void findGuestByNameEmptyTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name(null).build();
        List<GuestDTO> guests = new ArrayList<>();
        guests.add(guestDTO);
        when(guestService.findByGuestName(any(String.class))).thenReturn(guests);
        mockMvc.perform(get("/guest/name/{name}", "  ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Name is not valid.")));
    }

    @Test
    public void findAllGuestsTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test").build();
        List<GuestDTO> guests = new ArrayList<>();
        guests.add(guestDTO);
        when(guestService.findAllGuests()).thenReturn(guests);
        mockMvc.perform(get("/guest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(guestDTO.getName()));
    }

    @Test
    public void findAllGuestsEmptyTest() throws Exception {
        List<GuestDTO> guests = new ArrayList<>();
        when(guestService.findAllGuests()).thenReturn(guests);
        mockMvc.perform(get("/guest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void updateGuestTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("Test1").build();
        when(guestService.updateGuest(any(Long.class), any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(put("/guest/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(guestDTO.getName()));
    }

    @Test
    public void updateGuestWithEmptyValueTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name("").build();
        when(guestService.updateGuest(any(Long.class), any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(put("/guest/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateGuestWithNullValueTest() throws Exception {
        GuestDTO guestDTO = GuestDTO.builder().id(1l).name(null).build();
        when(guestService.updateGuest(any(Long.class), any(GuestDTO.class))).thenReturn(guestDTO);
        mockMvc.perform(put("/guest/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(guestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteGuestByIdTest() throws Exception {
        String message = "Successfully deleted guest.";
        when(guestService.isGuestExist(1l)).thenReturn(true);
        when(guestService.deleteGuestByGuestId(any(Long.class))).thenReturn(message);
        mockMvc.perform(get("/guest/{id}", 1l).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGuestByIdEmptyTest() throws Exception {
        when(guestService.isGuestExist(1l)).thenReturn(false);
        mockMvc.perform(get("/guest/{id}", 1l).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Guest not found.")));
    }
}
