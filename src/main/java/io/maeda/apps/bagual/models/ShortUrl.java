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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(exclude = {"created", "modified"})
@Entity(name = "short_urls")
@Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "alias_id", nullable = false, length = 11)
    private Long aliasId;

    @NonNull
    @ManyToOne(optional = false)
    private Url url;

    @ManyToOne
    private Company company;

    @NonNull
    @Column(name = "alias", nullable = false, length = 32)
    private String aliasName;

    @NonNull
    @Column(name = "shortcut", nullable = false, length = 32)
    private String shortcut;

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
    }

    public String getShortUrl() {
        return "http://" + this.aliasName + "/" + this.shortcut;
    }
}
