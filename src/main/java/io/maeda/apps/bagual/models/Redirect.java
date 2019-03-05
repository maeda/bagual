package io.maeda.apps.bagual.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.TimeZone;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(exclude = {"created", "modified", "deletedDate"})
@Entity(name = "redirects")
@Builder
public class Redirect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "short_url_id", nullable = false)
    private ShortUrl shortUrl;

    @Column(name = "http_user_agent")
    private String httpUserAgent;

    @NonNull
    @Column(name = "request_time", length = 11, nullable = false)
    private Long requestTime;

    @Builder.Default
    @Column(name = "http_referer", length = 128)
    private String httpReferer = "";

    @Column(name = "remote_addr", length = 32)
    private String remoteAddr;

    @Column(name = "redirect_status", length = 3)
    private String redirectStatus;

    @Column(name = "country", length = 64)
    private String country;

    @Column(name = "city", length = 64)
    private String city;

    @Column(name = "coordinates", length = 32)
    private String coordinates;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @NonNull
    @Builder.Default
    @Column(name = "created", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @NonNull
    @Builder.Default
    @Column(name = "modified", nullable = false)
    private LocalDateTime modified = LocalDateTime.now();

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        created = created == null ? now : created;
        modified = modified == null ? now : modified;

        requestTime = requestTime == null ? getRequestTimeCalculated(now) : requestTime;
    }

    @PreUpdate
    void preUpdate() {
        modified = LocalDateTime.now();
    }

    public static Long getRequestTimeCalculated(LocalDateTime now) {
        return now.atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli() / 1000;
    }
}
