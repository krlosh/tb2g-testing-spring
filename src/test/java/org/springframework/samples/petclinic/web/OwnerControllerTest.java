package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(this.clinicService);
    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        willAnswer(invocation ->{
            Owner owner = invocation.getArgument(0);
            owner.setId(1);
            return null;
        }).given(this.clinicService).saveOwner(any(Owner.class));
        this.mockMvc.perform(post("/owners/new")
                        .param("firstName", "John")
                        .param("lastName", "Foo")
                        .param("address", "123 Key West Florida")
                        .param("city","Key West")
                        .param("telephone", "123123123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void testNewOwnerPostNoyValid() throws Exception {
        this.mockMvc.perform(post("/owners/new")
                .param("firstName", "John")
                .param("lastName", "Foo")
                .param("city","Key West"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
        then(this.clinicService).should(never()).saveOwner(any(Owner.class));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        this.mockMvc.perform(get("/owners")
                            .param("lastName", "DONT FIND ME!!!"))
            .andExpect(status().isOk())
            .andExpect(view().name("owners/findOwners"));
        then(this.clinicService).should().findOwnerByLastName("DONT FIND ME!!!");
    }

    @Test
    void testFindAll() throws Exception{
        given(this.clinicService.findOwnerByLastName("")).willReturn(List.of(new Owner(), new Owner()));
        this.mockMvc.perform(get("/owners"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("owners/ownersList"));
        then(this.clinicService).should().findOwnerByLastName(this.stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("");
    }

    @Test
    void testFindWithEmptyLastName() throws Exception{
        given(this.clinicService.findOwnerByLastName("")).willReturn(List.of(new Owner(), new Owner()));
        this.mockMvc.perform(get("/owners")
                .param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));
        then(this.clinicService).should().findOwnerByLastName("");
    }

    @Test
    void testFindOneLastName() throws Exception{
        Owner owner = new Owner();
        owner.setId(1);
        given(this.clinicService.findOwnerByLastName("OnlyReturnsOne")).willReturn(List.of(owner));
        this.mockMvc.perform(get("/owners")
                .param("lastName", "OnlyReturnsOne"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        then(this.clinicService).should().findOwnerByLastName("OnlyReturnsOne");
    }

    @Test
    void testInitCreationForm() throws Exception {

        this.mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}