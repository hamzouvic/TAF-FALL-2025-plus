package ca.etsmtl.taf.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "t_project")
@Getter
@Setter
public class Project {

    @Id
    private String id;

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;

    @DBRef
    private TestUser owner;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;
}
