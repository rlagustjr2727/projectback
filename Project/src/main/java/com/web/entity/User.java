package com.web.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
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

    @Column(name = "USER_DOMAIN", nullable = false)
    @JsonProperty("userDomain")
    private String userDomain;

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
}
