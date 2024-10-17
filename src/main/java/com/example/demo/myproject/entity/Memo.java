package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name="tbl_memo")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 1000)
    private String memoText;

    @Column
    private String imageUrl;

    @Column
    private String videoUrl;
}
