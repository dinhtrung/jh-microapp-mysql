package com.ft.web.rest;

import com.ft.AppApp;

import com.ft.domain.HttpProfile;
import com.ft.repository.HttpProfileRepository;
import com.ft.service.HttpProfileService;
import com.ft.service.dto.HttpProfileDTO;
import com.ft.service.mapper.HttpProfileMapper;
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
 * Test class for the HttpProfileResource REST controller.
 *
 * @see HttpProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class HttpProfileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBBBBBBB";

    @Autowired
    private HttpProfileRepository httpProfileRepository;

    @Autowired
    private HttpProfileMapper httpProfileMapper;
    
    @Autowired
    private HttpProfileService httpProfileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHttpProfileMockMvc;

    private HttpProfile httpProfile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HttpProfileResource httpProfileResource = new HttpProfileResource(httpProfileService);
        this.restHttpProfileMockMvc = MockMvcBuilders.standaloneSetup(httpProfileResource)
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
    public static HttpProfile createEntity(EntityManager em) {
        HttpProfile httpProfile = new HttpProfile()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .configuration(DEFAULT_CONFIGURATION)
            .endpoint(DEFAULT_ENDPOINT);
        return httpProfile;
    }

    @Before
    public void initTest() {
        httpProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createHttpProfile() throws Exception {
        int databaseSizeBeforeCreate = httpProfileRepository.findAll().size();

        // Create the HttpProfile
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(httpProfile);
        restHttpProfileMockMvc.perform(post("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the HttpProfile in the database
        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeCreate + 1);
        HttpProfile testHttpProfile = httpProfileList.get(httpProfileList.size() - 1);
        assertThat(testHttpProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHttpProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHttpProfile.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
        assertThat(testHttpProfile.getEndpoint()).isEqualTo(DEFAULT_ENDPOINT);
    }

    @Test
    @Transactional
    public void createHttpProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = httpProfileRepository.findAll().size();

        // Create the HttpProfile with an existing ID
        httpProfile.setId(1L);
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(httpProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHttpProfileMockMvc.perform(post("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HttpProfile in the database
        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = httpProfileRepository.findAll().size();
        // set the field null
        httpProfile.setName(null);

        // Create the HttpProfile, which fails.
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(httpProfile);

        restHttpProfileMockMvc.perform(post("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isBadRequest());

        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndpointIsRequired() throws Exception {
        int databaseSizeBeforeTest = httpProfileRepository.findAll().size();
        // set the field null
        httpProfile.setEndpoint(null);

        // Create the HttpProfile, which fails.
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(httpProfile);

        restHttpProfileMockMvc.perform(post("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isBadRequest());

        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHttpProfiles() throws Exception {
        // Initialize the database
        httpProfileRepository.saveAndFlush(httpProfile);

        // Get all the httpProfileList
        restHttpProfileMockMvc.perform(get("/api/http-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(httpProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION.toString())))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT.toString())));
    }
    
    @Test
    @Transactional
    public void getHttpProfile() throws Exception {
        // Initialize the database
        httpProfileRepository.saveAndFlush(httpProfile);

        // Get the httpProfile
        restHttpProfileMockMvc.perform(get("/api/http-profiles/{id}", httpProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(httpProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION.toString()))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHttpProfile() throws Exception {
        // Get the httpProfile
        restHttpProfileMockMvc.perform(get("/api/http-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHttpProfile() throws Exception {
        // Initialize the database
        httpProfileRepository.saveAndFlush(httpProfile);

        int databaseSizeBeforeUpdate = httpProfileRepository.findAll().size();

        // Update the httpProfile
        HttpProfile updatedHttpProfile = httpProfileRepository.findById(httpProfile.getId()).get();
        // Disconnect from session so that the updates on updatedHttpProfile are not directly saved in db
        em.detach(updatedHttpProfile);
        updatedHttpProfile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .configuration(UPDATED_CONFIGURATION)
            .endpoint(UPDATED_ENDPOINT);
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(updatedHttpProfile);

        restHttpProfileMockMvc.perform(put("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isOk());

        // Validate the HttpProfile in the database
        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeUpdate);
        HttpProfile testHttpProfile = httpProfileList.get(httpProfileList.size() - 1);
        assertThat(testHttpProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHttpProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHttpProfile.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testHttpProfile.getEndpoint()).isEqualTo(UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    public void updateNonExistingHttpProfile() throws Exception {
        int databaseSizeBeforeUpdate = httpProfileRepository.findAll().size();

        // Create the HttpProfile
        HttpProfileDTO httpProfileDTO = httpProfileMapper.toDto(httpProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHttpProfileMockMvc.perform(put("/api/http-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(httpProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HttpProfile in the database
        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHttpProfile() throws Exception {
        // Initialize the database
        httpProfileRepository.saveAndFlush(httpProfile);

        int databaseSizeBeforeDelete = httpProfileRepository.findAll().size();

        // Get the httpProfile
        restHttpProfileMockMvc.perform(delete("/api/http-profiles/{id}", httpProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HttpProfile> httpProfileList = httpProfileRepository.findAll();
        assertThat(httpProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HttpProfile.class);
        HttpProfile httpProfile1 = new HttpProfile();
        httpProfile1.setId(1L);
        HttpProfile httpProfile2 = new HttpProfile();
        httpProfile2.setId(httpProfile1.getId());
        assertThat(httpProfile1).isEqualTo(httpProfile2);
        httpProfile2.setId(2L);
        assertThat(httpProfile1).isNotEqualTo(httpProfile2);
        httpProfile1.setId(null);
        assertThat(httpProfile1).isNotEqualTo(httpProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HttpProfileDTO.class);
        HttpProfileDTO httpProfileDTO1 = new HttpProfileDTO();
        httpProfileDTO1.setId(1L);
        HttpProfileDTO httpProfileDTO2 = new HttpProfileDTO();
        assertThat(httpProfileDTO1).isNotEqualTo(httpProfileDTO2);
        httpProfileDTO2.setId(httpProfileDTO1.getId());
        assertThat(httpProfileDTO1).isEqualTo(httpProfileDTO2);
        httpProfileDTO2.setId(2L);
        assertThat(httpProfileDTO1).isNotEqualTo(httpProfileDTO2);
        httpProfileDTO1.setId(null);
        assertThat(httpProfileDTO1).isNotEqualTo(httpProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(httpProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(httpProfileMapper.fromId(null)).isNull();
    }
}
