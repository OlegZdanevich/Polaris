package edu.bsu.polaris.model;

import edu.bsu.polaris.model.audit.UserDateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "project")
@Data
public class Project extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 140)
    private String name;


    @Column(name = "file_name")
    @Size(max=140)
    private String fileName;
}
