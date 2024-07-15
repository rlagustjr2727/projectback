package com.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "wboard")
public class Wboard {

    @Id
    @Column(name = "wboard_name", nullable = false)
    @JsonProperty("wboard_name")
    private String wboardName;

    @Column(name = "wboard_img", length = 255)
    @JsonProperty("wboard_img")
    private String wboardImg;

    @Column(name = "wboard_info", length = 1000)
    @JsonProperty("wboard_info")
    private String wboardInfo;

    @Column(name = "wboard_tip", length = 1000)
    @JsonProperty("wboard_tip")
    private String wboardTip;

    @Column(name = "wboard_type", length = 20)
    @JsonProperty("wboard_type")
    private String wboardType;

    @Column(name = "wboard_origin", length = 20)
    @JsonProperty("wboard_origin")
    private String wboardOrigin;

    @Column(name = "wboard_abv", length = 20)
    @JsonProperty("wboard_abv")
    private String wboardAbv;

    @Column(name = "wboard_yo", length = 20)
    @JsonProperty("wboard_yo")
    private String wboardYo;

    @Column(name = "wboard_abv_type", length = 10)
    @JsonProperty("wboard_abv_type")
    private String wboardAbvType;
    
    @Column(name = "wboard_of_type", length = 20)
    @JsonProperty("wboard_of_type")
    private String wboardOfType;
}
