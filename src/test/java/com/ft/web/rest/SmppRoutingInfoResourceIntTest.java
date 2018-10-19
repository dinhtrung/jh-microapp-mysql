package com.ft.web.rest;

import com.ft.AppApp;

import com.ft.domain.SmppRoutingInfo;
import com.ft.repository.SmppRoutingInfoRepository;
import com.ft.service.SmppRoutingInfoService;
import com.ft.service.dto.SmppRoutingInfoDTO;
import com.ft.service.mapper.SmppRoutingInfoMapper;
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
 * Test class for the SmppRoutingInfoResource REST controller.
 *
 * @see SmppRoutingInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class SmppRoutingInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SHORTCODE = "AAAAAAAAAA";
    private static final String UPDATED_SHORTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private SmppRoutingInfoRepository smppRoutingInfoRepository;

    @Autowired
    private SmppRoutingInfoMapper smppRoutingInfoMapper;
    
    @Autowired
    private SmppRoutingInfoService smppRoutingInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSmppRoutingInfoMockMvc;

    private SmppRoutingInfo smppRoutingInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmppRoutingInfoResource smppRoutingInfoResource = new SmppRoutingInfoResource(smppRoutingInfoService);
        this.restSmppRoutingInfoMockMvc = MockMvcBuilders.standaloneSetup(smppRoutingInfoResource)
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
    public static SmppRoutingInfo createEntity(EntityManager em) {
        SmppRoutingInfo smppRoutingInfo = new SmppRoutingInfo()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .shortcode(DEFAULT_SHORTCODE)
            .keywords(DEFAULT_KEYWORDS)
            .status(DEFAULT_STATUS);
        return smppRoutingInfo;
    }

    @Before
    public void initTest() {
        smppRoutingInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmppRoutingInfo() throws Exception {
        int databaseSizeBeforeCreate = smppRoutingInfoRepository.findAll().size();

        // Create the SmppRoutingInfo
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);
        restSmppRoutingInfoMockMvc.perform(post("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the SmppRoutingInfo in the database
        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SmppRoutingInfo testSmppRoutingInfo = smppRoutingInfoList.get(smppRoutingInfoList.size() - 1);
        assertThat(testSmppRoutingInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmppRoutingInfo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSmppRoutingInfo.getShortcode()).isEqualTo(DEFAULT_SHORTCODE);
        assertThat(testSmppRoutingInfo.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testSmppRoutingInfo.isStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSmppRoutingInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smppRoutingInfoRepository.findAll().size();

        // Create the SmppRoutingInfo with an existing ID
        smppRoutingInfo.setId(1L);
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmppRoutingInfoMockMvc.perform(post("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmppRoutingInfo in the database
        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = smppRoutingInfoRepository.findAll().size();
        // set the field null
        smppRoutingInfo.setName(null);

        // Create the SmppRoutingInfo, which fails.
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);

        restSmppRoutingInfoMockMvc.perform(post("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = smppRoutingInfoRepository.findAll().size();
        // set the field null
        smppRoutingInfo.setShortcode(null);

        // Create the SmppRoutingInfo, which fails.
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);

        restSmppRoutingInfoMockMvc.perform(post("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = smppRoutingInfoRepository.findAll().size();
        // set the field null
        smppRoutingInfo.setStatus(null);

        // Create the SmppRoutingInfo, which fails.
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);

        restSmppRoutingInfoMockMvc.perform(post("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmppRoutingInfos() throws Exception {
        // Initialize the database
        smppRoutingInfoRepository.saveAndFlush(smppRoutingInfo);

        // Get all the smppRoutingInfoList
        restSmppRoutingInfoMockMvc.perform(get("/api/smpp-routing-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smppRoutingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].shortcode").value(hasItem(DEFAULT_SHORTCODE.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSmppRoutingInfo() throws Exception {
        // Initialize the database
        smppRoutingInfoRepository.saveAndFlush(smppRoutingInfo);

        // Get the smppRoutingInfo
        restSmppRoutingInfoMockMvc.perform(get("/api/smpp-routing-infos/{id}", smppRoutingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smppRoutingInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.shortcode").value(DEFAULT_SHORTCODE.toString()))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSmppRoutingInfo() throws Exception {
        // Get the smppRoutingInfo
        restSmppRoutingInfoMockMvc.perform(get("/api/smpp-routing-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmppRoutingInfo() throws Exception {
        // Initialize the database
        smppRoutingInfoRepository.saveAndFlush(smppRoutingInfo);

        int databaseSizeBeforeUpdate = smppRoutingInfoRepository.findAll().size();

        // Update the smppRoutingInfo
        SmppRoutingInfo updatedSmppRoutingInfo = smppRoutingInfoRepository.findById(smppRoutingInfo.getId()).get();
        // Disconnect from session so that the updates on updatedSmppRoutingInfo are not directly saved in db
        em.detach(updatedSmppRoutingInfo);
        updatedSmppRoutingInfo
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .shortcode(UPDATED_SHORTCODE)
            .keywords(UPDATED_KEYWORDS)
            .status(UPDATED_STATUS);
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(updatedSmppRoutingInfo);

        restSmppRoutingInfoMockMvc.perform(put("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isOk());

        // Validate the SmppRoutingInfo in the database
        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeUpdate);
        SmppRoutingInfo testSmppRoutingInfo = smppRoutingInfoList.get(smppRoutingInfoList.size() - 1);
        assertThat(testSmppRoutingInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmppRoutingInfo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSmppRoutingInfo.getShortcode()).isEqualTo(UPDATED_SHORTCODE);
        assertThat(testSmppRoutingInfo.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testSmppRoutingInfo.isStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSmppRoutingInfo() throws Exception {
        int databaseSizeBeforeUpdate = smppRoutingInfoRepository.findAll().size();

        // Create the SmppRoutingInfo
        SmppRoutingInfoDTO smppRoutingInfoDTO = smppRoutingInfoMapper.toDto(smppRoutingInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmppRoutingInfoMockMvc.perform(put("/api/smpp-routing-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smppRoutingInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmppRoutingInfo in the database
        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmppRoutingInfo() throws Exception {
        // Initialize the database
        smppRoutingInfoRepository.saveAndFlush(smppRoutingInfo);

        int databaseSizeBeforeDelete = smppRoutingInfoRepository.findAll().size();

        // Get the smppRoutingInfo
        restSmppRoutingInfoMockMvc.perform(delete("/api/smpp-routing-infos/{id}", smppRoutingInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmppRoutingInfo> smppRoutingInfoList = smppRoutingInfoRepository.findAll();
        assertThat(smppRoutingInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmppRoutingInfo.class);
        SmppRoutingInfo smppRoutingInfo1 = new SmppRoutingInfo();
        smppRoutingInfo1.setId(1L);
        SmppRoutingInfo smppRoutingInfo2 = new SmppRoutingInfo();
        smppRoutingInfo2.setId(smppRoutingInfo1.getId());
        assertThat(smppRoutingInfo1).isEqualTo(smppRoutingInfo2);
        smppRoutingInfo2.setId(2L);
        assertThat(smppRoutingInfo1).isNotEqualTo(smppRoutingInfo2);
        smppRoutingInfo1.setId(null);
        assertThat(smppRoutingInfo1).isNotEqualTo(smppRoutingInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmppRoutingInfoDTO.class);
        SmppRoutingInfoDTO smppRoutingInfoDTO1 = new SmppRoutingInfoDTO();
        smppRoutingInfoDTO1.setId(1L);
        SmppRoutingInfoDTO smppRoutingInfoDTO2 = new SmppRoutingInfoDTO();
        assertThat(smppRoutingInfoDTO1).isNotEqualTo(smppRoutingInfoDTO2);
        smppRoutingInfoDTO2.setId(smppRoutingInfoDTO1.getId());
        assertThat(smppRoutingInfoDTO1).isEqualTo(smppRoutingInfoDTO2);
        smppRoutingInfoDTO2.setId(2L);
        assertThat(smppRoutingInfoDTO1).isNotEqualTo(smppRoutingInfoDTO2);
        smppRoutingInfoDTO1.setId(null);
        assertThat(smppRoutingInfoDTO1).isNotEqualTo(smppRoutingInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smppRoutingInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smppRoutingInfoMapper.fromId(null)).isNull();
    }
}
