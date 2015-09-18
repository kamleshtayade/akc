package com.spring.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.app.domain.util.CustomLocalDateSerializer;
import com.spring.app.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.spring.app.domain.enumeration.Eventstatus;
import com.spring.app.domain.enumeration.JudgepanelStatus;
import com.spring.app.domain.enumeration.Entrylimittype;

/**
 * A Event.
 */
@Entity
@Table(name = "EVENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false)
    private Eventstatus eventStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "judgepanel_status", nullable = false)
    private JudgepanelStatus judgepanelStatus;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "changed", nullable = false)
    private LocalDate changed;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "entry_count")
    private Integer entryCount;

    @Column(name = "entry_limit")
    private Integer entryLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_limit_type")
    private Entrylimittype entryLimitType;

    @ManyToOne
    private EventSite site;

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

    public EventSite getSite() {
        return site;
    }

    public void setSite(EventSite eventsite) {
        this.site = eventsite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if ( ! Objects.equals(id, event.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
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
