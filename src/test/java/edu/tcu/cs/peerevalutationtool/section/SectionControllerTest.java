package edu.tcu.cs.peerevalutationtool.section;

import edu.tcu.cs.peerevalutationtool.section.dto.SectionDto;
import edu.tcu.cs.peerevalutationtool.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SectionService sectionService;

    @Autowired
    ObjectMapper objectMapper;

    List<Section> sections;

    @BeforeEach
    void setUp() {
        this.sections = new ArrayList<>();

        Section sec1 = new Section();
        sec1.setId("Section 2017-2018");
        sec1.setYear("2017-2018");
        this.sections.add(sec1);

        Section sec2 = new Section();
        sec2.setId("Section 2018-2019");
        sec2.setYear("2018-2019");
        this.sections.add(sec2);

        Section sec3 = new Section();
        sec3.setId("Section 2019-2020");
        sec3.setYear("2019-2020");
        this.sections.add(sec3);

        Section sec4 = new Section();
        sec4.setId("Section 2020-2021");
        sec4.setYear("2020-2021");
        this.sections.add(sec4);

        Section sec5 = new Section();
        sec5.setId("Section 2021-2022");
        sec5.setYear("2021-2022");
        this.sections.add(sec5);

        Section sec6 = new Section();
        sec6.setId("Section 2022-2023");
        sec6.setYear("2022-2023");
        this.sections.add(sec6);

        Section sec7 = new Section();
        sec7.setId("Section 2023-2024");
        sec7.setYear("2023-2024");
        this.sections.add(sec7);

        Section sec8 = new Section();
        sec8.setId("Section 2023-2024-2");
        sec8.setYear("2023-2024");
        this.sections.add(sec8);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindSectionByIdSuccess() throws Exception {
        // Given
        given(this.sectionService.findById("Section 2023-2024")).willReturn(this.sections.get(6));

        // When and then
        this.mockMvc.perform(get("/api/v1/sections/Section 2023-2024").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("Section 2023-2024"));
    }

    @Test
    void testFindSectionByIdNotFound() throws Exception {
        // Given
        given(this.sectionService.findById("Section 2023-2024")).willThrow(new SectionNotFoundException("Section 2023-2024"));

        // When and then
        this.mockMvc.perform(get("/api/v1/sections/Section 2023-2024").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find section with name Section 2023-2024 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testFindAllSectionsSuccess() throws Exception {
        // Given
        given(this.sectionService.findAll()).willReturn(this.sections);

        // When and Then
        this.mockMvc.perform(get("/api/v1/sections").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.sections.size())))
                .andExpect(jsonPath("$.data[0].id").value("Section 2017-2018"))
                .andExpect(jsonPath("$.data[0].year").value("2017-2018"))
                .andExpect(jsonPath("$.data[1].id").value("Section 2018-2019"))
                .andExpect(jsonPath("$.data[1].year").value("2018-2019"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testFindAllSectionsByYearSuccess() throws Exception {
        // Given
        given(this.sectionService.findAllByYear("2023-2024")).willReturn(this.sections.subList(6,8));

        // When and Then
        this.mockMvc.perform(get("/api/v1/sections/allbyyear/2023-2024").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All By Year Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value("Section 2023-2024"))
                .andExpect(jsonPath("$.data[0].year").value("2023-2024"))
                .andExpect(jsonPath("$.data[1].id").value("Section 2023-2024-2"))
                .andExpect(jsonPath("$.data[1].year").value("2023-2024"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testAddSectionSuccess() throws Exception {
        // Given
        SectionDto sectionDto = new SectionDto("Section 2023-2024",
                                                "2023-2024",
                                                "08/21/23",
                                                "05/01/24",
                                                null);
        String json = this.objectMapper.writeValueAsString(sectionDto);

        Section savedSection = new Section();
        savedSection.setId("Section 2023-2024");
        savedSection.setYear("2023-2024");
        savedSection.setFirstDate("08/21/23");
        savedSection.setLastDate("05/01/24");

        given(this.sectionService.save(Mockito.any(Section.class))).willReturn(savedSection);

        // When and then
        this.mockMvc.perform(post("/api/v1/sections").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.year").value("2023-2024"))
                .andExpect(jsonPath("$.data.firstDate").value("08/21/23"))
                .andExpect(jsonPath("$.data.lastDate").value("05/01/24"));

    }

    @Test
    void testUpdateSectionSuccess() throws Exception {
        SectionDto sectionDto = new SectionDto("Section 2023-2024",
                "2023-2024",
                "08/21/23",
                "05/01/24",
                null);
        String json = this.objectMapper.writeValueAsString(sectionDto);

        Section updatedSection = new Section();
        updatedSection.setId("Section 2023-2024");
        updatedSection.setYear("2023-2024");
        updatedSection.setFirstDate("08/20/24");
        updatedSection.setLastDate("05/01/24");

        given(this.sectionService.update(eq("Section 2023-2024"), Mockito.any(Section.class))).willReturn(updatedSection);

        // When and then
        this.mockMvc.perform(put("/api/v1/sections/Section 2023-2024").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("Section 2023-2024"))
                .andExpect(jsonPath("$.data.firstDate").value(updatedSection.getFirstDate()))
                .andExpect(jsonPath("$.data.lastDate").value(updatedSection.getLastDate()));
    }

    @Test
    void testUpdateSectionErrorWithNonExistentId() throws Exception {
        SectionDto sectionDto = new SectionDto("Section 2023-2024",
                "2023-2024",
                "08/21/23",
                "05/01/24",
                null);
        String json = this.objectMapper.writeValueAsString(sectionDto);

        given(this.sectionService.update(eq("Section 2023-2024"), Mockito.any(Section.class))).willThrow(new SectionNotFoundException("Section 2023-2024"));

        // When and then
        this.mockMvc.perform(put("/api/v1/sections/Section 2023-2024").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find section with name Section 2023-2024 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}