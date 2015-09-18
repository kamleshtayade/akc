package com.spring.app.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.app.domain.util.CustomLocalDateSerializer;
import com.spring.app.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.spring.app.domain.enumeration.Eventstatus;
import com.spring.app.domain.enumeration.JudgepanelStatus;
import com.spring.app.domain.enumeration.Entrylimittype;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 20)
    private String name;

    @NotNull
    private Integer number;

    @NotNull
    private Eventstatus eventStatus;

    @NotNull
    private JudgepanelStatus judgepanelStatus;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    private LocalDate changed;

    @NotNull
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    private LocalDate startDate;

    @NotNull
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    private LocalDate endDate;

    private Integer entryCount;

    private Integer entryLimit;

    private Entrylimittype entryLimitType;

    private Long siteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Eventstatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Eventstatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public JudgepanelStatus getJudgepanelStatus() {
        return judgepanelStatus;
    }

    public void setJudgepanelStatus(JudgepanelStatus judgepanelStatus) {
        this.judgepanelStatus = judgepanelStatus;
    }

    public LocalDate getChanged() {
        return changed;
    }

    public void setChanged(LocalDate changed) {
        this.changed = changed;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(Integer entryCount) {
        this.entryCount = entryCount;
    }

    public Integer getEntryLimit() {
        return entryLimit;
    }

    public void setEntryLimit(Integer entryLimit) {
        this.entryLimit = entryLimit;
    }

    public Entrylimittype getEntryLimitType() {
        return entryLimitType;
    }

    public void setEntryLimitType(Entrylimittype entryLimitType) {
        this.entryLimitType = entryLimitType;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long eventsiteId) {
        this.siteId = eventsiteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;

        if ( ! Objects.equals(id, eventDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", number='" + number + "'" +
                ", eventStatus='" + eventStatus + "'" +
                ", judgepanelStatus='" + judgepanelStatus + "'" +
                ", changed='" + changed + "'" +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                ", entryCount='" + entryCount + "'" +
                ", entryLimit='" + entryLimit + "'" +
                ", entryLimitType='" + entryLimitType + "'" +
                '}';
    }
}
