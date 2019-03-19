package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClinicService clinicService;

    @Mock
    private HashMap<String, Object> model;

    @InjectMocks
    private VetController controller;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    void testShowVetList() throws Exception {
        this.mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
    }

    @Test
    void showVetList() {
        //given:
        given(this.clinicService.findVets()).willReturn(List.of(new Vet(), new Vet()));


        //when:
        String templateName = this.controller.showVetList(model);

        //then:
        then(this.clinicService).should().findVets();
        then(this.model).should().put(anyString(), any());
        assertThat("vets/vetList").isEqualTo(templateName);
    }

    @Test
    void showResourcesVetList() {
        //given:
        given(this.clinicService.findVets()).willReturn(List.of(new Vet(), new Vet()));


        //when:
        Vets vets = this.controller.showResourcesVetList();

        //then:
        then(this.clinicService).should().findVets();
        assertThat(vets).isNotNull();
        assertThat(vets.getVetList()).hasSize(2);
    }
}