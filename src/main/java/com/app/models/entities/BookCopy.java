package com.app.models.entities;

import com.app.models.enums.BookCondition;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_copy")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_copy_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false, length = 50)
    private BookCondition condition;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
