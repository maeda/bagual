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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(exclude = {"created", "modified"})
@Entity(name = "seeds")
@Builder
public class Seed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alias_id", nullable = false)
    private Alias alias;

    @Builder.Default
    @Column(name = "seed", length = 20)
    private long seed = 0;

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
}
