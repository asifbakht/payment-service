package com.social.auth.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "date_created", updatable = false, nullable = false)
    private String dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated")
    private String dateUpdated;
}
