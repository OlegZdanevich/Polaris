package edu.bsu.polaris.payload;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ProjectRequest {
    @NotBlank
    @Size(max = 140)
    private String project;
}
