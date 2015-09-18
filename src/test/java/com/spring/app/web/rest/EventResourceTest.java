package com.spring.app.web.rest;

import com.spring.app.Application;
import com.spring.app.domain.Event;
import com.spring.app.repository.EventRepository;
import com.spring.app.repository.search.EventSearchRepository;
import com.spring.app.web.rest.mapper.EventMapper;

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

import com.spring.app.domain.enumeration.Eventstatus;
import com.spring.app.domain.enumeration.JudgepanelStatus;
import com.spring.app.domain.enumeration.Entrylimittype;

/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMBER = 0;
    private static final Integer UPDATED_NUMBER = 1;

    private static final Eventstatus DEFAULT_EVENT_STATUS = Eventstatus.initiate;
    private static final Eventstatus UPDATED_EVENT_STATUS = Eventstatus.plan;

    private static final JudgepanelStatus DEFAULT_JUDGEPANEL_STATUS = JudgepanelStatus.allowted;
    private static final JudgepanelStatus UPDATED_JUDGEPANEL_STATUS = JudgepanelStatus.pending;

    private static final LocalDate DEFAULT_CHANGED = new LocalDate(0L);
    private static final LocalDate UPDATED_CHANGED = new LocalDate();

    private static final LocalDate DEFAULT_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_START_DATE = new LocalDate();

    private static final LocalDate DEFAULT_END_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_END_DATE = new LocalDate();

    private static final Integer DEFAULT_ENTRY_COUNT = 0;
    private static final Integer UPDATED_ENTRY_COUNT = 1;

    private static final Integer DEFAULT_ENTRY_LIMIT = 0;
    private static final Integer UPDATED_ENTRY_LIMIT = 1;

    private static final Entrylimittype DEFAULT_ENTRY_LIMIT_TYPE = Entrylimittype.fixed_numeric_limits;
    private static final Entrylimittype UPDATED_ENTRY_LIMIT_TYPE = Entrylimittype.no_plan;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventMapper eventMapper;

    @Inject
    private EventSearchRepository eventSearchRepository;

    private MockMvc restEventMockMvc;

    private Event event;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventResource eventResource = new EventResource();
        ReflectionTestUtils.setField(eventResource, "eventRepository", eventRepository);
        ReflectionTestUtils.setField(eventResource, "eventMapper", eventMapper);
        ReflectionTestUtils.setField(eventResource, "eventSearchRepository", eventSearchRepository);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource).build();
    }

    @Before
    public void initTest() {
        event = new Event();
        event.setName(DEFAULT_NAME);
        event.setNumber(DEFAULT_NUMBER);
        event.setEventStatus(DEFAULT_EVENT_STATUS);
        event.setJudgepanelStatus(DEFAULT_JUDGEPANEL_STATUS);
        event.setChanged(DEFAULT_CHANGED);
        event.setStartDate(DEFAULT_START_DATE);
        event.setEndDate(DEFAULT_END_DATE);
        event.setEntryCount(DEFAULT_ENTRY_COUNT);
        event.setEntryLimit(DEFAULT_ENTRY_LIMIT);
        event.setEntryLimitType(DEFAULT_ENTRY_LIMIT_TYPE);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = events.get(events.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvent.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testEvent.getEventStatus()).isEqualTo(DEFAULT_EVENT_STATUS);
        assertThat(testEvent.getJudgepanelStatus()).isEqualTo(DEFAULT_JUDGEPANEL_STATUS);
        assertThat(testEvent.getChanged()).isEqualTo(DEFAULT_CHANGED);
        assertThat(testEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEvent.getEntryCount()).isEqualTo(DEFAULT_ENTRY_COUNT);
        assertThat(testEvent.getEntryLimit()).isEqualTo(DEFAULT_ENTRY_LIMIT);
        assertThat(testEvent.getEntryLimitType()).isEqualTo(DEFAULT_ENTRY_LIMIT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setName(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setNumber(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventStatus(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJudgepanelStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setJudgepanelStatus(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setStartDate(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEndDate(null);

        // Create the Event, which fails.
        restEventMockMvc.perform(post("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the events
        restEventMockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].eventStatus").value(hasItem(DEFAULT_EVENT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].judgepanelStatus").value(hasItem(DEFAULT_JUDGEPANEL_STATUS.toString())))
                .andExpect(jsonPath("$.[*].changed").value(hasItem(DEFAULT_CHANGED.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].entryCount").value(hasItem(DEFAULT_ENTRY_COUNT)))
                .andExpect(jsonPath("$.[*].entryLimit").value(hasItem(DEFAULT_ENTRY_LIMIT)))
                .andExpect(jsonPath("$.[*].entryLimitType").value(hasItem(DEFAULT_ENTRY_LIMIT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.eventStatus").value(DEFAULT_EVENT_STATUS.toString()))
            .andExpect(jsonPath("$.judgepanelStatus").value(DEFAULT_JUDGEPANEL_STATUS.toString()))
            .andExpect(jsonPath("$.changed").value(DEFAULT_CHANGED.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.entryCount").value(DEFAULT_ENTRY_COUNT))
            .andExpect(jsonPath("$.entryLimit").value(DEFAULT_ENTRY_LIMIT))
            .andExpect(jsonPath("$.entryLimitType").value(DEFAULT_ENTRY_LIMIT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

		int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        event.setName(UPDATED_NAME);
        event.setNumber(UPDATED_NUMBER);
        event.setEventStatus(UPDATED_EVENT_STATUS);
        event.setJudgepanelStatus(UPDATED_JUDGEPANEL_STATUS);
        event.setChanged(UPDATED_CHANGED);
        event.setStartDate(UPDATED_START_DATE);
        event.setEndDate(UPDATED_END_DATE);
        event.setEntryCount(UPDATED_ENTRY_COUNT);
        event.setEntryLimit(UPDATED_ENTRY_LIMIT);
        event.setEntryLimitType(UPDATED_ENTRY_LIMIT_TYPE);
        restEventMockMvc.perform(put("/api/events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(event)))
                .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = events.get(events.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testEvent.getEventStatus()).isEqualTo(UPDATED_EVENT_STATUS);
        assertThat(testEvent.getJudgepanelStatus()).isEqualTo(UPDATED_JUDGEPANEL_STATUS);
        assertThat(testEvent.getChanged()).isEqualTo(UPDATED_CHANGED);
        assertThat(testEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getEntryCount()).isEqualTo(UPDATED_ENTRY_COUNT);
        assertThat(testEvent.getEntryLimit()).isEqualTo(UPDATED_ENTRY_LIMIT);
        assertThat(testEvent.getEntryLimitType()).isEqualTo(UPDATED_ENTRY_LIMIT_TYPE);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

		int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(databaseSizeBeforeDelete - 1);
    }
}
