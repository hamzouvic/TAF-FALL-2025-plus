package ca.etsmtl.taf.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "t_test_user")
@Getter
@Setter
public class TestUser {

    @Id
    private String id;

    @DBRef
    private User systemUser;

    @DBRef
    private Set<Project> projects;
}