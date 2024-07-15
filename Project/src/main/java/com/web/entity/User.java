package com.web.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"favorites"}) // 추가된 부분
public class User {
    @Id
    @Column(name = "USER_ID", nullable = false)
    @JsonProperty("userId")
    private String userId;

    @Column(name = "USER_NAME", nullable = false)
    @JsonProperty("userName")
    private String userName;

    @Column(name = "USER_NICKNAME", nullable = false, unique = true)
    @JsonProperty("userNickName")
    private String userNickName;

    @Column(name = "USER_PASSWORD", nullable = false)
    @JsonProperty("userPassword")
    private String userPassword;
    
    @Column(name = "USER_EMAIL", nullable = false)
    @JsonProperty("userEmail")
    private String userEmail;

    @Temporal(TemporalType.DATE)
    @Column(name = "USER_BIRTH", nullable = false)
    @JsonProperty("userBirth")
    private Date userBirth;

    @Column(name = "USER_PHONE_NUM", nullable = false)
    @JsonProperty("userPhoneNum")
    private String userPhoneNum;

    @Column(name = "USER_PROFILE_IMAGE")
    @JsonProperty("userProfileImage")
    private String userProfileImage;
    
    @Column(name = "USER_ROLE", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @JsonProperty("userRole")
    private Integer userRole = 0;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Favorite> favorites;
}
