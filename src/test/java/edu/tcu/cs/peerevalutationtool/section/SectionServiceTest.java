package edu.tcu.cs.peerevalutationtool.section;

import edu.tcu.cs.peerevalutationtool.admin.Admin;
import edu.tcu.cs.peerevalutationtool.section.utils.IdWorker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    SectionRepository sectionRepository;

    @InjectMocks
    SectionService sectionService;

    List<Section> sections;

    @BeforeEach
    void setUp() {
        Section sec1 = new Section();
        sec1.setId("Section 2022-2023");
        sec1.setYear("2023-2024");

        Section sec2 = new Section();
        sec2.setId("Section 2023-2024");
        sec2.setYear("2023-2024");

        this.sections = new ArrayList<>();
        this.sections.add(sec1);
        this.sections.add(sec2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByNameSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object sectionRepository.
        /*
		    “name”: “Senior Section 2023-2024”,
		    “year”: “2023-2024”,
         */
        Section sec = new Section();
        sec.setId("Section 2023-2024");
        sec.setYear("2023-2024");

        Admin adm = new Admin();
        adm.setId(1);
        adm.setName("Bingyang Wei");

        sec.setOverseer(adm);

        given(sectionRepository.findById("Section 2023-2024")).willReturn(Optional.of(sec)); // Defines the behavior of the mock object.

        // When. Act on the target behavior. When steps should cover the method to be tested.
        Section returnedSection = sectionService.findById("Section 2023-2024");

        // Then. Assert expected outcomes.
        assertThat(returnedSection.getId()).isEqualTo(sec.getId());
        assertThat(returnedSection.getYear()).isEqualTo(sec.getYear());
        verify(sectionRepository, times(1)).findById("Section 2023-2024");

    }

    @Test
    void testFindByNameNotFound(){
        // Given.
        given(sectionRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When.
        Throwable thrown = catchThrowable(()->{
            Section returnedSection = sectionService.findById("Section 2023-2024");
        });

        // Then.
        assertThat(thrown)
                .isInstanceOf(SectionNotFoundException.class)
                .hasMessage("Could not find section with name Section 2023-2024 :(");
        verify(sectionRepository, times(1)).findById("Section 2023-2024");

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testFindAllSuccess(){
        // Given
        given(sectionRepository.findAll()).willReturn(this.sections);

        // When
        List<Section> actualSections = sectionService.findAll();

        // Then
        assertThat(actualSections.size()).isEqualTo(this.sections.size());
        verify(sectionRepository, times(1)).findAll();
    }

    @Test
    void testFindAllByYearSuccess(){
        // Given
        given(sectionRepository.findAllByYear("2023-2024")).willReturn(this.sections);

        // When
        List<Section> actualSections = sectionService.findAllByYear("2023-2024");

        // Then
        assertThat(actualSections.size()).isEqualTo(this.sections.size());
        verify(sectionRepository, times(1)).findAllByYear("2023-2024");
    }

    @Test
    void testSaveSuccess(){
        // Given
        Section newSection = new Section();
        newSection.setId("Section 2023-2024");
        newSection.setYear("2023-2024");

        given(sectionRepository.save(newSection)).willReturn(newSection);

        // When
        Section savedSection = sectionService.save(newSection);

        // Then
        assertThat(savedSection.getId()).isEqualTo("Section 2023-2024");
        assertThat(savedSection.getYear()).isEqualTo("2023-2024");
        verify(sectionRepository, times(1)).save(newSection);
    }

    @Test
    void testUpdateSuccess(){
        // Given
        Section oldSection = new Section();
        oldSection.setId("Section 2024-2025");
        oldSection.setYear("2024-2025");
        oldSection.setFirstDate("08/21/24");
        oldSection.setLastDate("05/01/25");

        Section update = new Section();
        update.setId("Section 2024-2025");
        update.setYear("2024-2025");
        update.setFirstDate("08/20/24");
        update.setLastDate("05/01/25");

        given(sectionRepository.findById("Section 2024-2025")).willReturn(Optional.of(oldSection));
        given(sectionRepository.save(oldSection)).willReturn(oldSection);

        // When
        Section updatedSection = sectionService.update("Section 2024-2025", update);

        // Then
        assertThat(updatedSection.getId()).isEqualTo(update.getId());
        assertThat(updatedSection.getFirstDate()).isEqualTo(update.getFirstDate());
        verify(sectionRepository, VerificationModeFactory.times(1)).findById("Section 2024-2025");
        verify(sectionRepository, VerificationModeFactory.times(1)).save(oldSection);
    }

    @Test
    void testUpdateNotFound(){
        // Given
        Section update = new Section();
        update.setYear("2023-2024");
        update.setFirstDate("08/20/23");
        update.setLastDate("05/01/24");

        given(sectionRepository.findById("Section 2023-2024")).willReturn(Optional.empty());

        // When
        assertThrows(SectionNotFoundException.class, ()->{
            sectionService.update("Section 2023-2024", update);
        });

        // Then
        verify(sectionRepository, VerificationModeFactory.times(1)).findById("Section 2023-2024");
    }

    @Test
    void testUpdateActiveWeeksSuccess(){
        /*// Given
        Section section = new Section();
        section.setId("Section 2023-2024");
        section.setYear("2023-2024");
        section.setFirstDate("08/21/23");
        section.setLastDate("09/21/23");

        given(sectionRepository.findById("Section 2023-2024")).willReturn(Optional.of(section));
        given(sectionRepository.save(section)).willReturn(section);

        // When
        HashSet<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(1);
        Section updatedSection = sectionService.updateActiveWeeks("Section 2023-2024", indicesToRemove);

        // Then
        assertThat(updatedSection.getId()).isEqualTo("Section 2023-2024");
        assertThat(updatedSection.getFirstDate()).isEqualTo("08/21/23");
        assertThat(updatedSection.getLastDate()).isEqualTo("09/21/23");
        verify(sectionRepository, VerificationModeFactory.times(1)).findById("Section 2023-2024");
        verify(sectionRepository, VerificationModeFactory.times(1)).save(section);*/

    }
}