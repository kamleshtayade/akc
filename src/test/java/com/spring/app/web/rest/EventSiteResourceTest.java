package com.spring.app.web.rest;

import com.spring.app.Application;
import com.spring.app.domain.EventSite;
import com.spring.app.repository.EventSiteRepository;
import com.spring.app.repository.search.EventSiteSearchRepository;
import com.spring.app.web.rest.mapper.EventSiteMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventSiteResource REST controller.
 *
 * @see EventSiteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventSiteResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMBER = 0;
    private static final Integer UPDATED_NUMBER = 1;

    private static final LocalDate DEFAULT_CHANGED = new LocalDate(0L);
    private static final LocalDate UPDATED_CHANGED = new LocalDate();

    @Inject
    private EventSiteRepository eventSiteRepository;

    @Inject
    private EventSiteMapper eventSiteMapper;

    @Inject
    private EventSiteSearchRepository eventSiteSearchRepository;

    private MockMvc restEventSiteMockMvc;

    private EventSite eventSite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventSiteResource eventSiteResource = new EventSiteResource();
        ReflectionTestUtils.setField(eventSiteResource, "eventSiteRepository", eventSiteRepository);
        ReflectionTestUtils.setField(eventSiteResource, "eventSiteMapper", eventSiteMapper);
        ReflectionTestUtils.setField(eventSiteResource, "eventSiteSearchRepository", eventSiteSearchRepository);
        this.restEventSiteMockMvc = MockMvcBuilders.standaloneSetup(eventSiteResource).build();
    }

    @Before
    public void initTest() {
        eventSite = new EventSite();
        eventSite.setName(DEFAULT_NAME);
        eventSite.setNumber(DEFAULT_NUMBER);
        eventSite.setChanged(DEFAULT_CHANGED);
    }

    @Test
    @Transactional
    public void createEventSite() throws Exception {
        int databaseSizeBeforeCreate = eventSiteRepository.findAll().size();

        // Create the EventSite
        restEventSiteMockMvc.perform(post("/api/eventSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventSite)))
                .andExpect(status().isCreated());

        // Validate the EventSite in the database
        List<EventSite> eventSites = eventSiteRepository.findAll();
        assertThat(eventSites).hasSize(databaseSizeBeforeCreate + 1);
        EventSite testEventSite = eventSites.get(eventSites.size() - 1);
        assertThat(testEventSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventSite.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testEventSite.getChanged()).isEqualTo(DEFAULT_CHANGED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventSiteRepository.findAll().size();
        // set the field null
        eventSite.setName(null);

        // Create the EventSite, which fails.
        restEventSiteMockMvc.perform(post("/api/eventSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventSite)))
                .andExpect(status().isBadRequest());

        List<EventSite> eventSites = eventSiteRepository.findAll();
        assertThat(eventSites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventSiteRepository.findAll().size();
        // set the field null
        eventSite.setNumber(null);

        // Create the EventSite, which fails.
        restEventSiteMockMvc.perform(post("/api/eventSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventSite)))
                .andExpect(status().isBadRequest());

        List<EventSite> eventSites = eventSiteRepository.findAll();
        assertThat(eventSites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventSites() throws Exception {
        // Initialize the database
        eventSiteRepository.saveAndFlush(eventSite);

        // Get all the eventSites
        restEventSiteMockMvc.perform(get("/api/eventSites"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eventSite.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].changed").value(hasItem(DEFAULT_CHANGED.toString())));
    }

    @Test
    @Transactional
    public void getEventSite() throws Exception {
        // Initialize the database
        eventSiteRepository.saveAndFlush(eventSite);

        // Get the eventSite
        restEventSiteMockMvc.perform(get("/api/eventSites/{id}", eventSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(eventSite.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.changed").value(DEFAULT_CHANGED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventSite() throws Exception {
        // Get the eventSite
        restEventSiteMockMvc.perform(get("/api/eventSites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventSite() throws Exception {
        // Initialize the database
        eventSiteRepository.saveAndFlush(eventSite);

		int databaseSizeBeforeUpdate = eventSiteRepository.findAll().size();

        // Update the eventSite
        eventSite.setName(UPDATED_NAME);
        eventSite.setNumber(UPDATED_NUMBER);
        eventSite.setChanged(UPDATED_CHANGED);
        restEventSiteMockMvc.perform(put("/api/eventSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventSite)))
                .andExpect(status().isOk());

        // Validate the EventSite in the database
        List<EventSite> eventSites = eventSiteRepository.findAll();
        assertThat(eventSites).hasSize(databaseSizeBeforeUpdate);
        EventSite testEventSite = eventSites.get(eventSites.size() - 1);
        assertThat(testEventSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventSite.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testEventSite.getChanged()).isEqualTo(UPDATED_CHANGED);
    }

    @Test
    @Transactional
    public void deleteEventSite() throws Exception {
        // Initialize the database
        eventSiteRepository.saveAndFlush(eventSite);

		int databaseSizeBeforeDelete = eventSiteRepository.findAll().size();

        // Get the eventSite
        restEventSiteMockMvc.perform(delete("/api/eventSites/{id}", eventSite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EventSite> eventSites = eventSiteRepository.findAll();
        assertThat(eventSites).hasSize(databaseSizeBeforeDelete - 1);
    }
}
