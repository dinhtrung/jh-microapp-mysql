package com.ft.web.rest;

import com.ft.AppApp;

import com.ft.domain.SmppProfile;
import com.ft.repository.SmppProfileRepository;
import com.ft.service.SmppProfileService;
import com.ft.service.dto.SmppProfileDTO;
import com.ft.service.mapper.SmppProfileMapper;
import com.ft.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SmppProfileResource REST controller.
 *
 * @see SmppProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class SmppProfileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    @Autowired
    private SmppProfileRepository smppProfileRepository;

    @Autowired
    private SmppProfileMapper smppProfileMapper;
    
    @Autowired
    private SmppProfileService smppProfileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSmppProfileMockMvc;

    private SmppProfile smppProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmppProfileResource smppProfileResource = new SmppProfileResource(smppProfileService);
        this.restSmppProfileMockMvc = MockMvcBuilders.standaloneSetup(smppProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmppProfile createEntity(EntityManager em) {
        SmppProfile smppProfile = new SmppProfile()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .configuration(DEFAULT_CONFIGURATION);
        return smppProfile;
    }

    @Before
    public void initTest() {
        smppProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmppProfile() throws Exception {
        int databaseSizeBeforeCreate = smppProfileRepository.findAll().size();

        // Create the SmppProfile
        SmppProfileDTO smppProfileDTO = smppProfileMapper.toDto(smppProfile);
        restSmppProfileMockMvc.perform(post("/api/smpp-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the SmppProfile in the database
        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeCreate + 1);
        SmppProfile testSmppProfile = smppProfileList.get(smppProfileList.size() - 1);
        assertThat(testSmppProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmppProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSmppProfile.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
    }

    @Test
    @Transactional
    public void createSmppProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smppProfileRepository.findAll().size();

        // Create the SmppProfile with an existing ID
        smppProfile.setId(1L);
        SmppProfileDTO smppProfileDTO = smppProfileMapper.toDto(smppProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmppProfileMockMvc.perform(post("/api/smpp-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmppProfile in the database
        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = smppProfileRepository.findAll().size();
        // set the field null
        smppProfile.setName(null);

        // Create the SmppProfile, which fails.
        SmppProfileDTO smppProfileDTO = smppProfileMapper.toDto(smppProfile);

        restSmppProfileMockMvc.perform(post("/api/smpp-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppProfileDTO)))
            .andExpect(status().isBadRequest());

        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmppProfiles() throws Exception {
        // Initialize the database
        smppProfileRepository.saveAndFlush(smppProfile);

        // Get all the smppProfileList
        restSmppProfileMockMvc.perform(get("/api/smpp-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smppProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION.toString())));
    }
    
    @Test
    @Transactional
    public void getSmppProfile() throws Exception {
        // Initialize the database
        smppProfileRepository.saveAndFlush(smppProfile);

        // Get the smppProfile
        restSmppProfileMockMvc.perform(get("/api/smpp-profiles/{id}", smppProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smppProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmppProfile() throws Exception {
        // Get the smppProfile
        restSmppProfileMockMvc.perform(get("/api/smpp-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmppProfile() throws Exception {
        // Initialize the database
        smppProfileRepository.saveAndFlush(smppProfile);

        int databaseSizeBeforeUpdate = smppProfileRepository.findAll().size();

        // Update the smppProfile
        SmppProfile updatedSmppProfile = smppProfileRepository.findById(smppProfile.getId()).get();
        // Disconnect from session so that the updates on updatedSmppProfile are not directly saved in db
        em.detach(updatedSmppProfile);
        updatedSmppProfile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .configuration(UPDATED_CONFIGURATION);
        SmppProfileDTO smppProfileDTO = smppProfileMapper.toDto(updatedSmppProfile);

        restSmppProfileMockMvc.perform(put("/api/smpp-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppProfileDTO)))
            .andExpect(status().isOk());

        // Validate the SmppProfile in the database
        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeUpdate);
        SmppProfile testSmppProfile = smppProfileList.get(smppProfileList.size() - 1);
        assertThat(testSmppProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmppProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSmppProfile.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
    }

    @Test
    @Transactional
    public void updateNonExistingSmppProfile() throws Exception {
        int databaseSizeBeforeUpdate = smppProfileRepository.findAll().size();

        // Create the SmppProfile
        SmppProfileDTO smppProfileDTO = smppProfileMapper.toDto(smppProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmppProfileMockMvc.perform(put("/api/smpp-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmppProfile in the database
        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmppProfile() throws Exception {
        // Initialize the database
        smppProfileRepository.saveAndFlush(smppProfile);

        int databaseSizeBeforeDelete = smppProfileRepository.findAll().size();

        // Get the smppProfile
        restSmppProfileMockMvc.perform(delete("/api/smpp-profiles/{id}", smppProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmppProfile> smppProfileList = smppProfileRepository.findAll();
        assertThat(smppProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmppProfile.class);
        SmppProfile smppProfile1 = new SmppProfile();
        smppProfile1.setId(1L);
        SmppProfile smppProfile2 = new SmppProfile();
        smppProfile2.setId(smppProfile1.getId());
        assertThat(smppProfile1).isEqualTo(smppProfile2);
        smppProfile2.setId(2L);
        assertThat(smppProfile1).isNotEqualTo(smppProfile2);
        smppProfile1.setId(null);
        assertThat(smppProfile1).isNotEqualTo(smppProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmppProfileDTO.class);
        SmppProfileDTO smppProfileDTO1 = new SmppProfileDTO();
        smppProfileDTO1.setId(1L);
        SmppProfileDTO smppProfileDTO2 = new SmppProfileDTO();
        assertThat(smppProfileDTO1).isNotEqualTo(smppProfileDTO2);
        smppProfileDTO2.setId(smppProfileDTO1.getId());
        assertThat(smppProfileDTO1).isEqualTo(smppProfileDTO2);
        smppProfileDTO2.setId(2L);
        assertThat(smppProfileDTO1).isNotEqualTo(smppProfileDTO2);
        smppProfileDTO1.setId(null);
        assertThat(smppProfileDTO1).isNotEqualTo(smppProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smppProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smppProfileMapper.fromId(null)).isNull();
    }
}
