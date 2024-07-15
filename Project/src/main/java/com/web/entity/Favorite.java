package com.web.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorites_seq")
    @SequenceGenerator(name = "favorites_seq", sequenceName = "favorites_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-favorites")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference("board-favorites")
    private Board board;
}
