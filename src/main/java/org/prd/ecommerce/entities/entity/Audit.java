package org.prd.ecommerce.entities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String action;
    private Long idRegister;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;
    private String tableName;
    @Column(columnDefinition = "TEXT", length = 1000)
    private String details;

    public Audit(String username, String action, Long idRegister, Date date, String tableName, String details) {
        this.username = username;
        this.action = action;
        this.idRegister = idRegister;
        this.date = date;
        this.tableName = tableName;
        this.details = details;
    }

    public Audit(String username, String action, Date date, String details) {
        this.username = username;
        this.action = action;
        this.date = date;
        this.details = details;
    }
}
