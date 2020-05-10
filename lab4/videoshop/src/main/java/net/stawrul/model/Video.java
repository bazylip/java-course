package net.stawrul.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * Klasa encyjna reprezentująca towar w sklepie (film).
 */
@Entity
@EqualsAndHashCode(of = "id")
@NamedQueries(value = {
        @NamedQuery(name = Video.FIND_ALL, query = "SELECT b FROM Video b")
})
public class Video {
    public static final String FIND_ALL = "Video.FIND_ALL";

    @Getter
    @Id
    UUID id = UUID.randomUUID();

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    Integer amount;
}
