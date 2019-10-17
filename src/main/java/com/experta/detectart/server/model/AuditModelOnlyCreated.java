package com.experta.detectart.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"receivedAt"},
        allowGetters = true
)
public abstract class AuditModelOnlyCreated implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_at", nullable = false, updatable = false)
    @CreatedDate
    private Date receivedAt;

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(final Date receivedAt) {
        this.receivedAt = receivedAt;
    }
}
