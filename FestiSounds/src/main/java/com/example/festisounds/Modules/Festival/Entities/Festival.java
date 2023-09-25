package com.example.festisounds.Modules.Festival.Entities;

import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "festivals")
public class Festival {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "details", nullable = false, length = 500)
    private String details;

    @Builder.Default
    @ManyToMany(mappedBy = "festivals")
    private Set<FestivalArtist> artists = new HashSet<>();

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "is_robbie_invited")
    private boolean isRobbieInvited;

    @Column(name = "image", nullable = true, length = 500)
    private String image;

    @Column(name = "created_on")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdOn;

    @Column(name = "last_updated_on")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant lastUpdatedOn;

    @Column(name = "organizer", nullable = false, length = 100)
    private String organizer;

    @Column(name = "website", nullable = false, length = 100)
    private String website;
}
